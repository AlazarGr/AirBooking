package airline.booking.dto.response;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AirportResponse {

    private Long id;
    private String iataCode;
    private String name;
    private String city;
}