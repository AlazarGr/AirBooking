package airline.booking.model;

import airline.booking.enums.FlightStatus;
import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "flight", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"flight_number", "departure_date_time"})
})
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    @ManyToOne
    @JoinColumn(name = "departure_airport_id", nullable = false)
    private Airport departureAirport;

    @ManyToOne
    @JoinColumn(name = "arrival_airport_id", nullable = false)
    private Airport arrivalAirport;

    @Column(name = "departure_date_time", nullable = false)
    private ZonedDateTime departureDateTime;

    @Column(name = "arrival_date_time", nullable = false)
    private ZonedDateTime arrivalDateTime;

    @Column(name = "economy_seats", nullable = false)
    private int economySeats;

    @Column(name = "business_seats", nullable = false)
    private int businessSeats;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FlightStatus status;

    public Flight() {}

    public Flight(String flightNumber, Airport departureAirport, Airport arrivalAirport,
                  ZonedDateTime departureDateTime, ZonedDateTime arrivalDateTime,
                  int economySeats, int businessSeats, FlightStatus status) {
        this.flightNumber = flightNumber;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
        this.economySeats = economySeats;
        this.businessSeats = businessSeats;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public Airport getDepartureAirport() { return departureAirport; }
    public void setDepartureAirport(Airport departureAirport) { this.departureAirport = departureAirport; }

    public Airport getArrivalAirport() { return arrivalAirport; }
    public void setArrivalAirport(Airport arrivalAirport) { this.arrivalAirport = arrivalAirport; }

    public ZonedDateTime getDepartureDateTime() { return departureDateTime; }
    public void setDepartureDateTime(ZonedDateTime departureDateTime) { this.departureDateTime = departureDateTime; }

    public ZonedDateTime getArrivalDateTime() { return arrivalDateTime; }
    public void setArrivalDateTime(ZonedDateTime arrivalDateTime) { this.arrivalDateTime = arrivalDateTime; }

    public int getEconomySeats() { return economySeats; }
    public void setEconomySeats(int economySeats) { this.economySeats = economySeats; }

    public int getBusinessSeats() { return businessSeats; }
    public void setBusinessSeats(int businessSeats) { this.businessSeats = businessSeats; }

    public FlightStatus getStatus() { return status; }
    public void setStatus(FlightStatus status) { this.status = status; }
}