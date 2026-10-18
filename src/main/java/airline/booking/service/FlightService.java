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
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final AirportService airportService;
    private final FlightMapper flightMapper;

    // ---------- вспомогательные методы ----------

    @Transactional(readOnly = true)
    public Flight getFlightEntityById(Long flightId) {
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

    // ---------- API ----------

    // PostFlights
    @Transactional
    public FlightResponse createFlight(FlightRequest request) {
        Airport departureAirport = airportService.getByIataCode(request.getDepartureAirportIataCode());
        Airport arrivalAirport = airportService.getByIataCode(request.getArrivalAirportIataCode());

        if (departureAirport.getId().equals(arrivalAirport.getId())) {
            throw new BusinessException("SAME_DEPARTURE_ARRIVAL_AIRPORT",
                    "Аэропорт отправления и аэропорт назначения не могут совпадать");
        }
        if (!request.getDepartureDateTime().isBefore(request.getArrivalDateTime())) {
            throw new BusinessException("INVALID_FLIGHT_TIMES",
                    "Время отправления должно быть меньше времени прибытия");
        }
        if (request.getDepartureDateTime().isBefore(ZonedDateTime.now())) {
            throw new BusinessException("DEPARTURE_IN_PAST",
                    "Нельзя создать рейс с датой отправления в прошлом");
        }
        if (flightRepository.existsByFlightNumberAndDepartureDateTime(
                request.getFlightNumber(), request.getDepartureDateTime())) {
            throw new BusinessException("DUPLICATE_FLIGHT",
                    "Рейс с таким номером и временем отправления уже существует");
        }

        Flight flight = flightMapper.toEntity(request, departureAirport, arrivalAirport);
        flight.setStatus(FlightStatus.SCHEDULED);
        Flight saved = flightRepository.save(flight);

        // броней ещё нет, доступно всё
        return flightMapper.toResponse(saved, saved.getEconomySeats(), saved.getBusinessSeats());
    }

    // GetFlights
    @Transactional(readOnly = true)
    public FlightResponse getFlightById(Long id) {
        return toResponse(getFlightEntityById(id));
    }

    @Transactional(readOnly = true)
    public List<FlightResponse> searchFlights(String from, String to, LocalDate date) {
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime start = date.atStartOfDay(zone);
        ZonedDateTime end = start.plusDays(1);

        return flightRepository.search(from, to, start, end).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public FlightResponse cancelFlight(Long id) {
        Flight flight = getFlightEntityById(id);

        if (flight.getStatus() == FlightStatus.CANCELLED) {
            throw new BusinessException("FLIGHT_ALREADY_CANCELLED", "Рейс уже отменён");
        }

        flight.setStatus(FlightStatus.CANCELLED);
        return toResponse(flightRepository.save(flight));
    }
}