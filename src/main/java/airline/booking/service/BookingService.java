package airline.booking.service;


import airline.booking.dto.request.BookingRequest;
import airline.booking.dto.request.PassengerRequest;
import airline.booking.dto.response.BookingResponse;
import airline.booking.dto.response.PassengerResponse;
import airline.booking.enums.BookingStatus;
import airline.booking.enums.FlightStatus;
import airline.booking.exception.BusinessException;
import airline.booking.exception.EntityNotFoundException;
import airline.booking.mapper.BookingMapper;
import airline.booking.model.Airport;
import airline.booking.model.Booking;
import airline.booking.model.Flight;
import airline.booking.model.Passenger;
import airline.booking.repository.AirportRepository;
import airline.booking.repository.BookingRepository;
import airline.booking.repository.FlightRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private static final String ALLOWED_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;

    @Autowired
    private final BookingRepository bookingRepository;
    @Autowired
    private final BookingMapper bookingMapper;
    @Autowired
    private final FlightService flightService;
    @Autowired
    private final AirportService airportService;

    public BookingService(
            BookingRepository bookingRepository,
            BookingMapper bookingMapper,
            AirportService airportService,
            FlightService flightService
    ) {

        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.flightService = flightService;
        this.airportService = airportService;
    }

    // Суппорт методы
    private String generateUniqueBookingCode() {
        String code;
        do {
            code = generateRandomCode();
        } while (bookingRepository.existsByBookingCode(code));
        return code;
    }

    private String generateRandomCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(ALLOWED_CHARS.length());
            sb.append(ALLOWED_CHARS.charAt(index));
        }
        return sb.toString();
    }

    private void validateNoDuplicateDocuments(List<PassengerRequest> passengers) {
        Set<String> documentNumbers = passengers.stream()
                .map(PassengerRequest::getDocumentNumber)
                .collect(Collectors.toSet());

        if (documentNumbers.size() != passengers.size()) {
            throw new BusinessException(
                    "DUPLICATE_PASSENGER_DOCUMENT",
                    "Номер документа пассажира не должен повторяться внутри одного бронирования"
            );
        }
    }


    // реализации API
    // @PostMapping("/bookings")
    public BookingResponse createBooking(BookingRequest request) {
        // 1. Найти рейс, иначе 404
        Flight flight = flightService.getFlightEntityById(request.getFlightId());

        // 2. Рейс должен быть SCHEDULED (правило 6: "создавать бронирования можно только на рейс в статусе SCHEDULED")
        if (flight.getStatus() != FlightStatus.SCHEDULED) {
            throw new BusinessException("FLIGHT_NOT_SCHEDULED", "Рейс недоступен для бронирования");
        }

        // 3. Нельзя бронировать после отправления рейса
        if (flight.getDepartureDateTime().isBefore(ZonedDateTime.now())) {
            throw new BusinessException("FLIGHT_ALREADY_DEPARTED", "Рейс уже отправлен");
        }

        // 4. Хватает ли мест в выбранном классе
        int available = flightService.getAvailableSeats(flight, request.getClassOfService());
        int requested = request.getPassengers().size();
        if (requested > available) {
            throw new BusinessException("NOT_ENOUGH_SEATS", "Недостаточно свободных мест в выбранном классе");
        }

        // 5. Документы пассажиров не повторяются внутри одной брони
        validateNoDuplicateDocuments(request.getPassengers());

        // 6. Один пассажир не может иметь несколько активных броней на тот же рейс
        for (PassengerRequest p : request.getPassengers()) {
            long alreadyBooked = bookingRepository
                    .countActiveBookingsForPassengerAndFlight(p.getDocumentNumber(), flight.getId());
            if (alreadyBooked > 0) {
                throw new BusinessException("PASSENGER_ALREADY_BOOKED",
                        "Пассажир уже имеет активное бронирование на этот рейс");
            }
        }

        // 7. Собрать Entity, проставить служебные поля
        Booking booking = bookingMapper.toEntity(request, flight);
        booking.setBookingCode(generateUniqueBookingCode());
        booking.setStatus(BookingStatus.CREATED);
        booking.setCreationDate(ZonedDateTime.now());

        // 8. Сохранить
        Booking saved = bookingRepository.save(booking);

        return bookingMapper.toResponse(saved);
    }

    // @GetMapping("/bookings/by-code/{bookingCode}")
    public BookingResponse getBookingByCode(String bookingCode) {
        Booking booking = bookingRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new EntityNotFoundException("Booking", "кодом", bookingCode));
        return bookingMapper.toResponse(booking);
    }

    // @PostMapping("/bookings/{id}/confirm")
    public BookingResponse confirmBooking(Long id) {
        // 1. Найти бронирование, иначе 404
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking","id", id));

        // 2. Проверить допустимость перехода состояния
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BusinessException("BOOKING_CANCELLED",
                    "Нельзя подтвердить отменённое бронирование");
        }
        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            throw new BusinessException("ALREADY_CONFIRMED",
                    "Бронирование уже подтверждено");
        }

        // 3. Выполнить переход CREATED -> CONFIRMED
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setConfirmationDate(ZonedDateTime.now());

        // 4. Сохранить и вернуть DTO
        Booking saved = bookingRepository.save(booking);
        return bookingMapper.toResponse(saved);
    }


    // @PostMapping("/bookings/{id}/cancel")
    public BookingResponse cancelBooking(Long id) {
        // 1. Найти бронирование, иначе 404
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking","id", id));

        // 2. Выполнить переход CREATED -> CONFIRMED
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setConfirmationDate(ZonedDateTime.now());

        // 4. Сохранить и вернуть DTO
        Booking saved = bookingRepository.save(booking);
        return bookingMapper.toResponse(saved);
    }
}
