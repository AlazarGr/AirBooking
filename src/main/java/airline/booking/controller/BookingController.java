package airline.booking.controller;

import airline.booking.dto.request.BookingRequest;
import airline.booking.dto.response.BookingResponse;
import airline.booking.service.AirportService;
import airline.booking.service.BookingService;
import airline.booking.service.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BookingController {

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;
    private final FlightService flightService;
    private final AirportService airportService;

    // Необходимые для задания API

    // Done
    // создание бронирования
    @PostMapping("/bookings")
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse createBooking(@Valid @RequestBody BookingRequest request) {
        log.info("createBooking");
        return bookingService.createBooking(request);
    }

    // Done
    // поиск бронирования по коду букинга
    @GetMapping("/bookings/by-code/{bookingCode}")
    public BookingResponse getBookingByCode(@PathVariable("bookingCode") String bookingCode){
        log.info("getBookingByCode");
        return bookingService.getBookingByCode(bookingCode);
    }

    // Done
    // подтверждение существующего бронирования
    @PostMapping("/bookings/{id}/confirm")
    public BookingResponse confirmBooking(@PathVariable Long id) {
        log.info("confirmBooking");
        return bookingService.confirmBooking(id);
    }

    // Done
    // отмена существующего бронирования
    @PostMapping("/bookings/{id}/cancel")
    public BookingResponse cancelBooking(@PathVariable Long id){
        log.info("cancelBooking");
        return bookingService.cancelBooking(id);
    }


}