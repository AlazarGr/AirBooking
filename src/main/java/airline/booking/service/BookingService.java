package airline.booking.service;

import airline.booking.dto.request.BookingRequest;
import airline.booking.dto.request.PassengerRequest;
import airline.booking.dto.response.BookingResponse;
import airline.booking.enums.BookingStatus;
import airline.booking.enums.FlightStatus;
import airline.booking.exception.BusinessException;
import airline.booking.exception.EntityNotFoundException;
import airline.booking.mapper.BookingMapper;
import airline.booking.model.AppUser;
import airline.booking.model.Booking;
import airline.booking.model.Flight;
import airline.booking.repository.AppUserRepository;
import airline.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private static final String ALLOWED_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;

    private final AppUserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final FlightService flightService;

    // ---------- вспомогательные методы ----------

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
            sb.append(ALLOWED_CHARS.charAt(random.nextInt(ALLOWED_CHARS.length())));
        }
        return sb.toString();
    }

    private void validateNoDuplicateDocuments(List<PassengerRequest> passengers) {
        Set<String> documentNumbers = passengers.stream()
                .map(PassengerRequest::getDocumentNumber)
                .collect(Collectors.toSet());

        if (documentNumbers.size() != passengers.size()) {
            throw new BusinessException("DUPLICATE_PASSENGER_DOCUMENT",
                    "Номер документа пассажира не должен повторяться внутри одного бронирования");
        }
    }

    // чужая бронь выглядит как несуществующая (404)
    private Booking getOwnedBooking(Long id, String username) {
        return bookingRepository.findByIdAndOwnerUsername(id, username)
                .orElseThrow(() -> new EntityNotFoundException("Booking", "id", id));
    }

    // ---------- API ----------

    // POST /bookings
    @Transactional
    public BookingResponse createBooking(BookingRequest request, String username) {
        Flight flight = flightService.getFlightEntityById(request.getFlightId());

        if (flight.getStatus() != FlightStatus.SCHEDULED) {
            throw new BusinessException("FLIGHT_NOT_SCHEDULED", "Рейс недоступен для бронирования");
        }
        if (flight.getDepartureDateTime().isBefore(ZonedDateTime.now())) {
            throw new BusinessException("FLIGHT_ALREADY_DEPARTED", "Рейс уже отправлен");
        }

        int available = flightService.getAvailableSeats(flight, request.getClassOfService());
        if (request.getPassengers().size() > available) {
            throw new BusinessException("NOT_ENOUGH_SEATS", "Недостаточно свободных мест в выбранном классе");
        }

        validateNoDuplicateDocuments(request.getPassengers());

        for (PassengerRequest p : request.getPassengers()) {
            long alreadyBooked = bookingRepository
                    .countActiveBookingsForPassengerAndFlight(p.getDocumentNumber(), flight.getId());
            if (alreadyBooked > 0) {
                throw new BusinessException("PASSENGER_ALREADY_BOOKED",
                        "Пассажир уже имеет активное бронирование на этот рейс");
            }
        }

        AppUser owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User", "username", username));

        Booking booking = bookingMapper.toEntity(request, flight);
        booking.setBookingCode(generateUniqueBookingCode());
        booking.setStatus(BookingStatus.CREATED);
        booking.setCreationDate(ZonedDateTime.now());
        booking.setOwner(owner);

        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    // GET /bookings/{id}
    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Long id, String username) {
        return bookingMapper.toResponse(getOwnedBooking(id, username));
    }

    // GET /bookings/by-code/{code}
    @Transactional(readOnly = true)
    public BookingResponse getBookingByCode(String bookingCode, String username) {
        Booking booking = bookingRepository.findByBookingCodeAndOwnerUsername(bookingCode, username)
                .orElseThrow(() -> new EntityNotFoundException("Booking", "кодом", bookingCode));
        return bookingMapper.toResponse(booking);
    }

    // GET /bookings
    @Transactional(readOnly = true)
    public List<BookingResponse> getBookings(Long flightId, String username) {
        List<Booking> bookings = flightId == null
                ? bookingRepository.findByOwnerUsername(username)
                : bookingRepository.findByOwnerUsernameAndFlightId(username, flightId);
        return bookings.stream().map(bookingMapper::toResponse).toList();
    }

    // POST /bookings/{id}/confirm   CREATED -> CONFIRMED
    @Transactional
    public BookingResponse confirmBooking(Long id, String username) {
        Booking booking = getOwnedBooking(id, username);

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BusinessException("BOOKING_CANCELLED", "Нельзя подтвердить отменённое бронирование");
        }
        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            throw new BusinessException("ALREADY_CONFIRMED", "Бронирование уже подтверждено");
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setConfirmationDate(ZonedDateTime.now());
        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    // POST /bookings/{id}/cancel   CREATED | CONFIRMED -> CANCELLED
    @Transactional
    public BookingResponse cancelBooking(Long id, String username) {
        Booking booking = getOwnedBooking(id, username);

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BusinessException("ALREADY_CANCELLED", "Бронирование уже отменено");
        }

        // места освобождаются сами: отменённые брони не учитываются при подсчёте
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancellationDate(ZonedDateTime.now());
        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    // PUT /bookings/{id}
    @Transactional
    public BookingResponse updateBooking(Long id, BookingRequest request, String username) {
        Booking booking = getOwnedBooking(id, username);

        if (booking.getStatus() != BookingStatus.CREATED) {
            throw new BusinessException("BOOKING_NOT_EDITABLE",
                    "Изменять можно только бронирование в статусе CREATED");
        }

        Flight flight = booking.getFlight();
        if (!flight.getId().equals(request.getFlightId())) {
            throw new BusinessException("FLIGHT_CHANGE_NOT_ALLOWED",
                    "Нельзя перенести бронирование на другой рейс, создайте новое");
        }
        if (flight.getStatus() != FlightStatus.SCHEDULED) {
            throw new BusinessException("FLIGHT_NOT_SCHEDULED", "Рейс недоступен для бронирования");
        }
        if (flight.getDepartureDateTime().isBefore(ZonedDateTime.now())) {
            throw new BusinessException("FLIGHT_ALREADY_DEPARTED", "Рейс уже отправлен");
        }

        // места, которые эта бронь уже занимает, считаются свободными для неё самой
        int available = flightService.getAvailableSeats(flight, request.getClassOfService());
        if (booking.getClassOfService() == request.getClassOfService()) {
            available += booking.getPassengers().size();
        }
        if (request.getPassengers().size() > available) {
            throw new BusinessException("NOT_ENOUGH_SEATS", "Недостаточно свободных мест в выбранном классе");
        }

        validateNoDuplicateDocuments(request.getPassengers());

        for (PassengerRequest p : request.getPassengers()) {
            long other = bookingRepository.countActiveBookingsForPassengerAndFlightExcluding(
                    p.getDocumentNumber(), flight.getId(), id);
            if (other > 0) {
                throw new BusinessException("PASSENGER_ALREADY_BOOKED",
                        "Пассажир уже имеет активное бронирование на этот рейс");
            }
        }

        bookingMapper.updateEntity(request, booking);
        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    // DELETE /bookings/{id}
    @Transactional
    public void deleteBooking(Long id, String username) {
        Booking booking = getOwnedBooking(id, username);

        if (booking.getStatus() != BookingStatus.CANCELLED) {
            throw new BusinessException("BOOKING_NOT_CANCELLED",
                    "Удалить можно только отменённое бронирование, сначала отмените его");
        }
        bookingRepository.delete(booking);
    }
}