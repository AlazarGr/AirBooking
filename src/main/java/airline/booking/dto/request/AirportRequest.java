package airline.booking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

//"LomBok"
public class AirportRequest {

    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$", message = "IATA-код должен состоять из 3 заглавных букв")
    private String iataCode;

    @NotBlank
    private String name;

    @NotBlank
    private String city;

    public String getIataCode() {
        return iataCode;
    }

    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}