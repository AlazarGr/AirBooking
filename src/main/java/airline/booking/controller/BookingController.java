package airline.booking.controller;

import airline.booking.dto.request.BookingRequest;
import airline.booking.dto.request.FlightRequest;
import airline.booking.dto.response.BookingResponse;
import airline.booking.dto.response.FlightResponse;
import airline.booking.model.Airport;
import airline.booking.model.Booking;
import airline.booking.service.AirportService;
import airline.booking.service.BookingService;
import airline.booking.model.Flight;
import airline.booking.service.FlightService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookingController {

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;
    private final FlightService flightService;
    private final AirportService airportService;

    public BookingController(
            BookingService bookingService,
            FlightService flightService,
            AirportService airportService
    ) {
        this.bookingService = bookingService;
        this.flightService = flightService;
        this.airportService = airportService;
    }



    // Позднее вырезать
    // <--
//    @GetMapping()
//    public String test(
//            ) {
//        log.info("test");
//        return bookingService.test();
//    }

    @GetMapping("/AirportAll")
    public List<Airport> getAllAirports(){
        log.info("Get all Airports");
        return airportService.getAllAirports();
    }

//    @PostMapping("/Airport")
//    public Airport addAirport(@RequestBody Airport airport){
//        log.info("Add Airport");
//        return bookingService.addAirport(airport);
//    }


    // <--




    // Необходимые для задания API

    // Done
    // создаёт новый рейс.
    @PostMapping("/flights")
    @ResponseStatus(HttpStatus.CREATED)
    public FlightResponse createFlight(@Valid @RequestBody FlightRequest request) {
        log.info("createFlight, {}", request);
        return flightService.createFlight(request);
    }

    // WIP
    // получить рейс по id
    @GetMapping("/flights/{id}")
    public Flight getFlightById(@PathVariable("id") Long id){
        throw  new UnsupportedOperationException("Not supported yet.");
    }

    // WIP
    // возвращает рейс по аэропорту отправки, назначения и даты
    @GetMapping("/flights?from={departureAirport}&to={arrivalAirport}&date={date}")
    public List<Flight> getFlightsByAirportAndDate(){

        throw  new UnsupportedOperationException("Not supported yet.");
    }

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

    // WIP
    // отмена существующего рейса
    @PostMapping("/flights/{id}/cancel")
    public void cancelFlight(@PathVariable("id") Long id){
        throw  new UnsupportedOperationException("Not supported yet.");
    }

}