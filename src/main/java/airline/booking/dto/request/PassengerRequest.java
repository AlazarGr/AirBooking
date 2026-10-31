package airline.booking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@RequiredArgsConstructor
public class PassengerRequest {
    @NotBlank
    private String fullName;

    @NotNull
    @Past
    private LocalDate birthDate;

    @NotBlank
    private String documentNumber;
}
