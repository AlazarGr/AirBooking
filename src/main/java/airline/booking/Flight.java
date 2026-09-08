package airline.booking;

import jakarta.persistence.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.ZonedDateTime;

@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long number;
    @ManyToOne
    @JoinColumn(name = "departure_airport_id")
    private Airport  departureAirport;
    @ManyToOne
    @JoinColumn(name = "arrival_airport_id")
    private Airport arrivalAirport;
    private ZonedDateTime departureDateTime;
    private ZonedDateTime arrivalDateTime;
    private Long numberOfEconomyClassSeats;
    private Long numberOfBusinessClassSeats;
    private FlightStatus status;


    public Flight() {}

    public Flight(String json){
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.readValue(json, Flight.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON format", e);
        }
    }

    public Flight(long number, Airport departureAirport, Airport arrivalAirport, ZonedDateTime departureDateTime, ZonedDateTime arrivalDateTime, Long numberOfEconomyClassSeats, Long numberOfBusinessClassSeats, FlightStatus status) {
        this.number = number;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
        this.numberOfEconomyClassSeats = numberOfEconomyClassSeats;
        this.numberOfBusinessClassSeats = numberOfBusinessClassSeats;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public ZonedDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(ZonedDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public ZonedDateTime getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(ZonedDateTime arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }

    public Long getNumberOfEconomyClassSeats() {
        return numberOfEconomyClassSeats;
    }

    public void setNumberOfEconomyClassSeats(Long numberOfEconomyClassSeats) {
        this.numberOfEconomyClassSeats = numberOfEconomyClassSeats;
    }

    public Long getNumberOfBusinessClassSeats() {
        return numberOfBusinessClassSeats;
    }

    public void setNumberOfBusinessClassSeats(Long numberOfBusinessClassSeats) {
        this.numberOfBusinessClassSeats = numberOfBusinessClassSeats;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }
}
