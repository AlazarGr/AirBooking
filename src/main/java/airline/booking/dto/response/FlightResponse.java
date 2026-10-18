package airline.booking.dto.response;

import airline.booking.enums.FlightStatus;

import java.time.ZonedDateTime;

public class FlightResponse {

    private Long id;
    private String flightNumber;
    private String departureAirportIataCode;
    private String arrivalAirportIataCode;
    private ZonedDateTime departureDateTime;
    private ZonedDateTime arrivalDateTime;
    private int economySeats;
    private int businessSeats;
    private int availableEconomySeats;
    private int availableBusinessSeats;
    private FlightStatus status;

    // геттеры/сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getDepartureAirportIataCode() { return departureAirportIataCode; }
    public void setDepartureAirportIataCode(String v) { this.departureAirportIataCode = v; }

    public String getArrivalAirportIataCode() { return arrivalAirportIataCode; }
    public void setArrivalAirportIataCode(String v) { this.arrivalAirportIataCode = v; }

    public ZonedDateTime getDepartureDateTime() { return departureDateTime; }
    public void setDepartureDateTime(ZonedDateTime v) { this.departureDateTime = v; }

    public ZonedDateTime getArrivalDateTime() { return arrivalDateTime; }
    public void setArrivalDateTime(ZonedDateTime v) { this.arrivalDateTime = v; }

    public int getEconomySeats() { return economySeats; }
    public void setEconomySeats(int v) { this.economySeats = v; }

    public int getBusinessSeats() { return businessSeats; }
    public void setBusinessSeats(int v) { this.businessSeats = v; }

    public int getAvailableEconomySeats() { return availableEconomySeats; }
    public void setAvailableEconomySeats(int v) { this.availableEconomySeats = v; }

    public int getAvailableBusinessSeats() { return availableBusinessSeats; }
    public void setAvailableBusinessSeats(int v) { this.availableBusinessSeats = v; }

    public FlightStatus getStatus() { return status; }
    public void setStatus(FlightStatus status) { this.status = status; }
}