package airline.booking.dto.request;

import airline.booking.enums.ClassOfService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@RequiredArgsConstructor
public class BookingRequest {
    private Long flightId;
    private ClassOfService classOfService;
    private List<PassengerRequest> passengers;
}