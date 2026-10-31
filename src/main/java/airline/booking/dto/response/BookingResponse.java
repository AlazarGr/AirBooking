package airline.booking.dto.response;

import airline.booking.enums.BookingStatus;
import airline.booking.enums.ClassOfService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;


@Getter
@Setter
@RequiredArgsConstructor
public class BookingResponse {

    private Long id;
    private String bookingCode;
    private Long flightId;
    private String flightNumber;        // немного контекста о рейсе, чтобы не заставлять клиента делать второй запрос
    private ClassOfService classOfService;
    private List<PassengerResponse> passengers;
    private BookingStatus status;
    private ZonedDateTime creationDate;
    private ZonedDateTime confirmationDate;
    private ZonedDateTime cancellationDate;

}