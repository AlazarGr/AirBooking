package airline.booking;

import java.time.ZonedDateTime;
import java.util.List;


public class Booking {
    private long id;
    private long uniqueBookingCode;
    private Flight flight;
    private ClassOfService classOfService;
    private List<Passenger> passengersList;
    private ZonedDateTime creationDate;
    private ZonedDateTime confirmationDate = null;
    private ZonedDateTime cancellationDate = null;
    private BookingStatus status;


}
