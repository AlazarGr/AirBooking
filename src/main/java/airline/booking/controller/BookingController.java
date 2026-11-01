package airline.booking.controller;

import airline.booking.dto.request.BookingRequest;
import airline.booking.dto.response.BookingResponse;
import airline.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse createBooking(@Valid @RequestBody BookingRequest request, Principal principal) {
        return bookingService.createBooking(request, principal.getName());
    }

    @GetMapping
    public List<BookingResponse> getBookings(
            @RequestParam(value = "flightId", required = false) Long flightId, Principal principal) {
        return bookingService.getBookings(flightId, principal.getName());
    }

    @GetMapping("/{id}")
    public BookingResponse getBooking(@PathVariable("id") Long id, Principal principal) {
        return bookingService.getBookingById(id, principal.getName());
    }

    @GetMapping("/by-code/{bookingCode}")
    public BookingResponse getBookingByCode(@PathVariable("bookingCode") String bookingCode,
                                            Principal principal) {
        return bookingService.getBookingByCode(bookingCode, principal.getName());
    }

    @PostMapping("/{id}/confirm")
    public BookingResponse confirmBooking(@PathVariable("id") Long id, Principal principal) {
        return bookingService.confirmBooking(id, principal.getName());
    }

    @PostMapping("/{id}/cancel")
    public BookingResponse cancelBooking(@PathVariable("id") Long id, Principal principal) {
        return bookingService.cancelBooking(id, principal.getName());
    }
}