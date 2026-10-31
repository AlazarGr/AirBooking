package airline.booking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@RequiredArgsConstructor
public class AirportRequest {

    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$", message = "IATA-код должен состоять из 3 заглавных букв")
    private String iataCode;

    @NotBlank
    private String name;

    @NotBlank
    private String city;
}