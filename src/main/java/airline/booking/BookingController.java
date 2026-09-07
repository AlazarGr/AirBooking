package airline.booking;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }




    @GetMapping()
    public String test(
            ) {
        return service.test();
    }


    //
    @PostMapping("/flights")
    public Flight addFlight(){
        throw  new UnsupportedOperationException("Not supported yet.");

    }

    @GetMapping("/flights/{id}")
    public Flight getFlightById(@PathVariable("id") Long id){
        throw  new UnsupportedOperationException("Not supported yet.");
    }

    @GetMapping("/flights?from={departureAirport}&to={arrivalAirport}&date={date}")
    public List<Flight> getFlights(){
        throw  new UnsupportedOperationException("Not supported yet.");
    }

    @PostMapping("/bookings")
    public Booking createBooking(@RequestBody Booking booking){
        throw  new UnsupportedOperationException("Not supported yet.");
    }

    @GetMapping("/bookings/{id}")
    public Booking getBookingById(@PathVariable("id") Long id){
        throw  new UnsupportedOperationException("Not supported yet.");
    }

    @GetMapping("/bookings/by-code/{bookingCode}")
    public List<Booking> getBookingByCode(@PathVariable("bookingCode") String bookingCode){
    throw  new UnsupportedOperationException("Not supported yet.");
    }

    @PostMapping("/bookings/{id}/confirm")
    public void confirmBooking(@PathVariable("id") Long id){
        throw  new UnsupportedOperationException("Not supported yet.");
    }

    @PostMapping("/bookings/{id}/cancel")
    public void cancelBooking(@PathVariable("id") Long id){
        throw  new UnsupportedOperationException("Not supported yet.");
    }

    @PostMapping("/flights/{id}/cancel")
    public void cancelFlight(@PathVariable("id") Long id){
        throw  new UnsupportedOperationException("Not supported yet.");
    }

}