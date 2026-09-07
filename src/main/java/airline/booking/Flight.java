package airline.booking;
import java.time.ZonedDateTime;

public class Flight {
    private long id;
    private long number;
    private Airport  departureAirport;
    private Airport arrivalAirport;
    private ZonedDateTime departureDateTime;
    private ZonedDateTime arrivalDateTime;
    private Long numberOfEconomyClassSeats;
    private Long numberOfBusinessClassSeats;
    private FlightStatus status;
}
