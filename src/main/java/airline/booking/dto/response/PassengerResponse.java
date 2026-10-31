package airline.booking.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@RequiredArgsConstructor
public class PassengerResponse {
    private String fullName;
    private LocalDate birthDate;
    private String documentNumber;
}