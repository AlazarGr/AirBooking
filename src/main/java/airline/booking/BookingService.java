package airline.booking;


import airline.booking.repository.AirportRepository;
import airline.booking.repository.BookingRepository;
import airline.booking.repository.FlightRepository;

import java.util.List;

@org.springframework.stereotype.Service
public class BookingService {
    private AirportRepository airportRepository;
    private BookingRepository bookingRepository;
    private FlightRepository flightRepository;

    public String test() {
        return "Hi";
    }

    public List<Airport> getAllAirports() {
        return airportRepository.findAll();
    }
    public List<Flight> getAllFlights(){
        return flightRepository.findAll();
    }
    public List<Booking> getAllBookings(){
        return bookingRepository.findAll();
    }

}
