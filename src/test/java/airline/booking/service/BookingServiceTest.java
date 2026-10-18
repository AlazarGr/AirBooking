package airline.booking.service;

import airline.booking.dto.request.BookingRequest;
import airline.booking.dto.request.PassengerRequest;
import airline.booking.dto.response.BookingResponse;
import airline.booking.enums.BookingStatus;
import airline.booking.enums.ClassOfService;
import airline.booking.enums.FlightStatus;
import airline.booking.exception.BusinessException;
import airline.booking.mapper.BookingMapper;
import airline.booking.model.Booking;
import airline.booking.model.Flight;
import airline.booking.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock private BookingRepository bookingRepository;
    @Mock private FlightService flightService;
    @Mock private BookingMapper bookingMapper;

    @InjectMocks private BookingService bookingService;

    private Flight flight;

    @BeforeEach
    void setUp() {
        flight = new Flight();
        flight.setId(1L);
        flight.setFlightNumber("SU100");
        flight.setStatus(FlightStatus.SCHEDULED);
        flight.setDepartureDateTime(ZonedDateTime.now().plusDays(10));
        flight.setArrivalDateTime(ZonedDateTime.now().plusDays(10).plusHours(3));
        flight.setEconomySeats(150);
        flight.setBusinessSeats(20);

        // значения по умолчанию; в конкретных тестах переопределяются
        lenient().when(flightService.getFlightEntityById(1L)).thenReturn(flight);
        lenient().when(flightService.getAvailableSeats(any(Flight.class), any(ClassOfService.class))).thenReturn(10);
        lenient().when(bookingRepository.existsByBookingCode(anyString())).thenReturn(false);
        lenient().when(bookingRepository.countActiveBookingsForPassengerAndFlight(anyString(), anyLong()))
                .thenReturn(0L);
        lenient().when(bookingMapper.toEntity(any(BookingRequest.class), any(Flight.class)))
                .thenAnswer(inv -> new Booking());
        lenient().when(bookingMapper.toResponse(any(Booking.class))).thenReturn(new BookingResponse());
        lenient().when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    // ---------- вспомогательные методы ----------

    private PassengerRequest passenger(String fullName, String document) {
        PassengerRequest p = new PassengerRequest();
        p.setFullName(fullName);
        p.setBirthDate(LocalDate.of(1990, 1, 1));   // поправь под имя поля в твоём DTO
        p.setDocumentNumber(document);
        return p;
    }

    private BookingRequest request(PassengerRequest... passengers) {
        BookingRequest r = new BookingRequest();
        r.setFlightId(1L);
        r.setClassOfService(ClassOfService.ECONOMY);
        r.setPassengers(List.of(passengers));
        return r;
    }

    private Booking existingBooking(BookingStatus status) {
        Booking b = new Booking();
        b.setId(5L);
        b.setStatus(status);
        b.setFlight(flight);
        return b;
    }

    // ---------- 1. успешное создание бронирования ----------

    @Test
    void createBooking_success() {
        BookingResponse response = bookingService.createBooking(request(passenger("Иван Иванов", "1234")));

        assertNotNull(response);
        ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingRepository).save(captor.capture());

        Booking saved = captor.getValue();
        assertEquals(BookingStatus.CREATED, saved.getStatus());
        assertNotNull(saved.getBookingCode());
        assertEquals(6, saved.getBookingCode().length());
        assertNotNull(saved.getCreationDate());
        assertNull(saved.getConfirmationDate());
        assertNull(saved.getCancellationDate());
    }

    // ---------- 2. попытка забронировать больше мест, чем доступно ----------

    @Test
    void createBooking_notEnoughSeats() {
        when(flightService.getAvailableSeats(flight, ClassOfService.ECONOMY)).thenReturn(1);

        BookingRequest request = request(passenger("Иван Иванов", "1234"), passenger("Пётр Петров", "5678"));

        BusinessException ex = assertThrows(BusinessException.class, () -> bookingService.createBooking(request));
        assertEquals("NOT_ENOUGH_SEATS", ex.getCode());
        verify(bookingRepository, never()).save(any());
    }

    // ---------- 3. бронирование нескольких пассажиров ----------

    @Test
    void createBooking_severalPassengers_occupyThatManySeats() {
        // свободно ровно 3 места, и три пассажира помещаются
        when(flightService.getAvailableSeats(flight, ClassOfService.ECONOMY)).thenReturn(3);

        BookingRequest request = request(
                passenger("Иван Иванов", "1111"),
                passenger("Пётр Петров", "2222"),
                passenger("Мария Сидорова", "3333"));

        assertDoesNotThrow(() -> bookingService.createBooking(request));
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void createBooking_duplicateDocumentsInOneBooking() {
        BookingRequest request = request(
                passenger("Иван Иванов", "1111"),
                passenger("Пётр Петров", "1111"));

        BusinessException ex = assertThrows(BusinessException.class, () -> bookingService.createBooking(request));
        assertEquals("DUPLICATE_PASSENGER_DOCUMENT", ex.getCode());
    }

    // ---------- 4. отмена бронирования ----------

    @Test
    void cancelBooking_success() {
        Booking booking = existingBooking(BookingStatus.CONFIRMED);
        when(bookingRepository.findById(5L)).thenReturn(Optional.of(booking));

        bookingService.cancelBooking(5L);

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        assertNotNull(booking.getCancellationDate());
        verify(bookingRepository).save(booking);
    }

    // ---------- 5. повторная отмена ----------

    @Test
    void cancelBooking_twice_fails() {
        Booking booking = existingBooking(BookingStatus.CANCELLED);
        when(bookingRepository.findById(5L)).thenReturn(Optional.of(booking));

        BusinessException ex = assertThrows(BusinessException.class, () -> bookingService.cancelBooking(5L));
        assertEquals("ALREADY_CANCELLED", ex.getCode());
        verify(bookingRepository, never()).save(any());
    }

    // ---------- 6. подтверждение бронирования ----------

    @Test
    void confirmBooking_success() {
        Booking booking = existingBooking(BookingStatus.CREATED);
        when(bookingRepository.findById(5L)).thenReturn(Optional.of(booking));

        bookingService.confirmBooking(5L);

        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        assertNotNull(booking.getConfirmationDate());
    }

    // ---------- 7. повторное подтверждение ----------

    @Test
    void confirmBooking_twice_fails() {
        Booking booking = existingBooking(BookingStatus.CONFIRMED);
        when(bookingRepository.findById(5L)).thenReturn(Optional.of(booking));

        BusinessException ex = assertThrows(BusinessException.class, () -> bookingService.confirmBooking(5L));
        assertEquals("ALREADY_CONFIRMED", ex.getCode());
    }

    @Test
    void confirmBooking_cancelled_fails() {
        Booking booking = existingBooking(BookingStatus.CANCELLED);
        when(bookingRepository.findById(5L)).thenReturn(Optional.of(booking));

        BusinessException ex = assertThrows(BusinessException.class, () -> bookingService.confirmBooking(5L));
        assertEquals("BOOKING_CANCELLED", ex.getCode());
    }

    // ---------- 8. бронирование отменённого рейса ----------

    @Test
    void createBooking_cancelledFlight_fails() {
        flight.setStatus(FlightStatus.CANCELLED);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> bookingService.createBooking(request(passenger("Иван Иванов", "1234"))));
        assertEquals("FLIGHT_NOT_SCHEDULED", ex.getCode());
        verify(bookingRepository, never()).save(any());
    }

    // ---------- 9. повторное бронирование одного пассажира на тот же рейс ----------

    @Test
    void createBooking_samePassengerTwice_fails() {
        when(bookingRepository.countActiveBookingsForPassengerAndFlight("1234", 1L)).thenReturn(1L);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> bookingService.createBooking(request(passenger("Иван Иванов", "1234"))));
        assertEquals("PASSENGER_ALREADY_BOOKED", ex.getCode());
        verify(bookingRepository, never()).save(any());
    }
}