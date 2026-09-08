package airline.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookingController {

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;

    public BookingController(BookingService service) {
        this.bookingService = service;
    }



    // Позднее вырезать
    // <--
    @GetMapping()
    public String test(
            ) {
        return bookingService.test();
    }

    @GetMapping("/AirportAll")
    public List<Airport> getAllAirports(){
        return bookingService.getAllAirports();
    }





    // <--




    // Необходимые для задания API
    // создаёт новый рейс.
    @PostMapping("/flights")
    public Flight addFlight(){
        throw  new UnsupportedOperationException("Not supported yet.");

    }
    // получить рейс по id
    @GetMapping("/flights/{id}")
    public Flight getFlightById(@PathVariable("id") Long id){
        throw  new UnsupportedOperationException("Not supported yet.");
    }

    // возвращает рейс по аэропорту отправки, назначения и даты
    @GetMapping("/flights?from={departureAirport}&to={arrivalAirport}&date={date}")
    public List<Flight> getFlights(){
        throw  new UnsupportedOperationException("Not supported yet.");
    }

    // создание бронирования
    @PostMapping("/bookings")
    public Booking createBooking(@RequestBody Booking booking){
        throw  new UnsupportedOperationException("Not supported yet.");
    }

    // поиск бронирования по id
    @GetMapping("/bookings/{id}")
    public Booking getBookingById(@PathVariable("id") Long id){
        throw  new UnsupportedOperationException("Not supported yet.");
    }

    // поиск бронрования по коду букинга
    @GetMapping("/bookings/by-code/{bookingCode}")
    public List<Booking> getBookingByCode(@PathVariable("bookingCode") String bookingCode){
    throw  new UnsupportedOperationException("Not supported yet.");
    }

    // подтверждение существующего бронирования
    @PostMapping("/bookings/{id}/confirm")
    public void confirmBooking(@PathVariable("id") Long id){
        throw  new UnsupportedOperationException("Not supported yet.");
    }

    // отмена существующего бронирования
    @PostMapping("/bookings/{id}/cancel")
    public void cancelBooking(@PathVariable("id") Long id){
        throw  new UnsupportedOperationException("Not supported yet.");
    }

    // отмена существующего рейса
    @PostMapping("/flights/{id}/cancel")
    public void cancelFlight(@PathVariable("id") Long id){
        throw  new UnsupportedOperationException("Not supported yet.");
    }

}