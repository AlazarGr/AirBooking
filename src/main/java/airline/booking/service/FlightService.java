package airline.booking.service;

import airline.booking.dto.request.FlightRequest;
import airline.booking.dto.response.FlightResponse;
import airline.booking.enums.ClassOfService;
import airline.booking.enums.FlightStatus;
import airline.booking.exception.BusinessException;
import airline.booking.exception.EntityNotFoundException;
import airline.booking.mapper.FlightMapper;
import airline.booking.model.Airport;
import airline.booking.model.Flight;
import airline.booking.repository.BookingRepository;
import airline.booking.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private static final ZonedDateTime MIN_DATE = ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    private static final ZonedDateTime MAX_DATE = ZonedDateTime.of(2100, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final AirportService airportService;
    private final FlightMapper flightMapper;

    // ---------- вспомогательные методы (нужны и BookingService) ----------

    @Transactional(readOnly = true)
    public Flight getEntityById(Long flightId) {
        return flightRepository.findById(flightId)
                .orElseThrow(() -> new EntityNotFoundException("Flight", "id", flightId));
    }

    public int getAvailableSeats(Flight flight, ClassOfService classOfService) {
        int totalSeats = classOfService == ClassOfService.ECONOMY
                ? flight.getEconomySeats()
                : flight.getBusinessSeats();

        int bookedSeats = (int) bookingRepository
                .countActivePassengersByFlightAndClass(flight.getId(), classOfService);

        return totalSeats - bookedSeats;
    }

    private FlightResponse toResponse(Flight flight) {
        return flightMapper.toResponse(
                flight,
                getAvailableSeats(flight, ClassOfService.ECONOMY),
                getAvailableSeats(flight, ClassOfService.BUSINESS)
        );
    }

    private void validateRoute(FlightRequest request, Airport departure, Airport arrival) {
        if (departure.getId().equals(arrival.getId())) {
            throw new BusinessException("SAME_DEPARTURE_ARRIVAL_AIRPORT",
                    "Аэропорт отправления и аэропорт назначения не могут совпадать");
        }
        if (!request.getDepartureDateTime().isBefore(request.getArrivalDateTime())) {
            throw new BusinessException("INVALID_FLIGHT_TIMES",
                    "Время отправления должно быть меньше времени прибытия");
        }
        if (request.getDepartureDateTime().isBefore(ZonedDateTime.now())) {
            throw new BusinessException("DEPARTURE_IN_PAST",
                    "Нельзя указать дату отправления в прошлом");
        }
    }

    // ---------- API ----------

    // POST /flights
    @Transactional
    public FlightResponse createFlight(FlightRequest request) {
        Airport departure = airportService.getByIataCode(request.getDepartureAirportIataCode());
        Airport arrival = airportService.getByIataCode(request.getArrivalAirportIataCode());
        validateRoute(request, departure, arrival);

        if (flightRepository.existsByFlightNumberAndDepartureDateTime(
                request.getFlightNumber(), request.getDepartureDateTime())) {
            throw new BusinessException("DUPLICATE_FLIGHT",
                    "Рейс с таким номером и временем отправления уже существует");
        }

        Flight flight = flightMapper.toEntity(request, departure, arrival);
        flight.setStatus(FlightStatus.SCHEDULED);
        Flight saved = flightRepository.save(flight);

        // броней ещё нет, доступно всё
        return flightMapper.toResponse(saved, saved.getEconomySeats(), saved.getBusinessSeats());
    }

    // GET /flights/{id}
    @Transactional(readOnly = true)
    public FlightResponse getFlightById(Long id) {
        return toResponse(getEntityById(id));
    }

    // GET /flights?from=&to=&date=  (+ необязательный arrivalDate)
    @Transactional(readOnly = true)
    public List<FlightResponse> searchFlights(String from, String to,
                                              LocalDate departureDate, LocalDate arrivalDate) {
        ZoneId zone = ZoneId.systemDefault();

        ZonedDateTime depStart = departureDate != null ? departureDate.atStartOfDay(zone) : MIN_DATE;
        ZonedDateTime depEnd = departureDate != null ? depStart.plusDays(1) : MAX_DATE;
        ZonedDateTime arrStart = arrivalDate != null ? arrivalDate.atStartOfDay(zone) : MIN_DATE;
        ZonedDateTime arrEnd = arrivalDate != null ? arrStart.plusDays(1) : MAX_DATE;

        return flightRepository.search(from, to, depStart, depEnd, arrStart, arrEnd).stream()
                .map(this::toResponse)
                .toList();
    }

    // GET /flights/all
    @Transactional(readOnly = true)
    public List<FlightResponse> getAll() {
        return flightRepository.findAll().stream().map(this::toResponse).toList();
    }

    // POST /flights/{id}/cancel   SCHEDULED -> CANCELLED
    @Transactional
    public FlightResponse cancelFlight(Long id) {
        Flight flight = getEntityById(id);

        if (flight.getStatus() == FlightStatus.CANCELLED) {
            throw new BusinessException("FLIGHT_ALREADY_CANCELLED", "Рейс уже отменён");
        }

        flight.setStatus(FlightStatus.CANCELLED);
        return toResponse(flightRepository.save(flight));
    }
}