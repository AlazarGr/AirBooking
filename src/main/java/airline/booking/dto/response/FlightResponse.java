package airline.booking.dto.response;

import airline.booking.enums.FlightStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;


@Getter
@Setter
@RequiredArgsConstructor
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

}