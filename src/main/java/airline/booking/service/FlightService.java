package airline.booking.service;

import airline.booking.dto.request.AirportRequest;
import airline.booking.dto.request.FlightRequest;
import airline.booking.dto.response.FlightResponse;
import airline.booking.enums.ClassOfService;
import airline.booking.enums.FlightStatus;
import airline.booking.exception.BusinessException;
import airline.booking.mapper.AirportMapper;
import airline.booking.mapper.FlightMapper;
import airline.booking.model.Airport;
import airline.booking.model.Flight;
import airline.booking.repository.AirportRepository;
import airline.booking.repository.BookingRepository;
import airline.booking.repository.FlightRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class FlightService {

    @Autowired
    private final FlightRepository flightRepository;
    @Autowired
    private final FlightMapper flightMapper;
    @Autowired
    private final BookingRepository bookingRepository;
    @Autowired
    private final AirportRepository airportRepository;
    @Autowired
    private final AirportService airportService;

    public  FlightService(
            FlightRepository flightRepository,
            BookingRepository bookingRepository,
            AirportRepository airportRepository,
            AirportService airportService,
            FlightMapper flightMapper
    ) {
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
        this.airportRepository = airportRepository;
        this.airportService = airportService;
        this.flightMapper = flightMapper;
    }


    // Суппорт методы
    public Flight getFlightEntityById(Long flightId) {
        return flightRepository.findById(flightId).get();
    }

    public int getAvailableSeats(Flight flight, ClassOfService classOfService) {
        int totalSeats = classOfService == ClassOfService.ECONOMY
                ? flight.getEconomySeats() : flight.getBusinessSeats();

        int bookedSeats = (int) bookingRepository.countActivePassengersByFlightAndClass(flight.getId(), classOfService);

        return totalSeats - bookedSeats;
    }



    // @PostMapping("/flights")
    public FlightResponse createFlight(FlightRequest request) {
        // 1. Найти аэропорты по IATA-коду, иначе 404
        Airport departureAirport = airportService.getByIataCode(request.getDepartureAirportIataCode());
        Airport arrivalAirport = airportService.getByIataCode(request.getArrivalAirportIataCode());

        // 2. Аэропорт отправления и назначения не могут совпадать (правило 1)
        if (departureAirport.getId().equals(arrivalAirport.getId())) {
            throw new BusinessException("SAME_DEPARTURE_ARRIVAL_AIRPORT",
                    "Аэропорт отправления и аэропорт назначения не могут совпадать");
        }

        // 3. Время отправления должно быть меньше времени прибытия (правило 2)
        if (!request.getDepartureDateTime().isBefore(request.getArrivalDateTime())) {
            throw new BusinessException("INVALID_FLIGHT_TIMES",
                    "Время отправления должно быть меньше времени прибытия");
        }

        // 4. Нельзя дату отправления в прошлом (правило 3)
        if (request.getDepartureDateTime().isBefore(ZonedDateTime.now())) {
            throw new BusinessException("DEPARTURE_IN_PAST",
                    "Нельзя создать рейс с датой отправления в прошлом");
        }

        // 5. Номер рейса + время отправления должны быть уникальны (правило 5)
        boolean duplicate = flightRepository.existsByFlightNumberAndDepartureDateTime(
                request.getFlightNumber(), request.getDepartureDateTime());
        if (duplicate) {
            throw new BusinessException("DUPLICATE_FLIGHT",
                    "Рейс с таким номером и временем отправления уже существует");
        }

        // 6. Собрать Entity, проставить служебные поля
        Flight flight = flightMapper.toEntity(request, departureAirport, arrivalAirport);
        flight.setStatus(FlightStatus.SCHEDULED);

        // 7. Сохранить
        Flight saved = flightRepository.save(flight);

        // 8. При создании доступные места = общим местам (броней ещё нет)
        return flightMapper.toResponse(saved, saved.getEconomySeats(), saved.getBusinessSeats());
    }

    // Реализация API

}
