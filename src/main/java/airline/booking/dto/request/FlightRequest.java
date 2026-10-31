package airline.booking.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;


@Getter
@Setter
@RequiredArgsConstructor
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

}