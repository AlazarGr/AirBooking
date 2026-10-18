package airline.booking.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.ZonedDateTime;

public class FlightRequest {

    @NotBlank
    private String flightNumber;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$", message = "IATA-код должен состоять из 3 заглавных букв")
    private String departureAirportIataCode;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$", message = "IATA-код должен состоять из 3 заглавных букв")
    private String arrivalAirportIataCode;

    @NotNull
    @FutureOrPresent
    private ZonedDateTime departureDateTime;

    @NotNull
    private ZonedDateTime arrivalDateTime;

    @NotNull
    @Min(0)
    private Integer economySeats;

    @NotNull
    @Min(0)
    private Integer businessSeats;


    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDepartureAirportIataCode() {
        return departureAirportIataCode;
    }

    public void setDepartureAirportIataCode(String departureAirportIataCode) {
        this.departureAirportIataCode = departureAirportIataCode;
    }

    public String getArrivalAirportIataCode() {
        return arrivalAirportIataCode;
    }

    public void setArrivalAirportIataCode(String arrivalAirportIataCode) {
        this.arrivalAirportIataCode = arrivalAirportIataCode;
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

    public Integer getEconomySeats() {
        return economySeats;
    }

    public void setEconomySeats(Integer economySeats) {
        this.economySeats = economySeats;
    }

    public Integer getBusinessSeats() {
        return businessSeats;
    }

    public void setBusinessSeats(Integer businessSeats) {
        this.businessSeats = businessSeats;
    }
}