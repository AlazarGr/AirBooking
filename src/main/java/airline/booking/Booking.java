package airline.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private long uniqueBookingCode;
    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;
    private ClassOfService classOfService;

    @ElementCollection 
    @CollectionTable(
            name = "booking_passenger", 
            joinColumns = @JoinColumn(name = "booking_id") 
    )
    @Column(nullable = false)
    private List<Passenger> booking_passenger;
    @Column(nullable = false)
    private ZonedDateTime creationDate;
    private ZonedDateTime confirmationDate = null;
    private ZonedDateTime cancellationDate = null;
    @Column(nullable = false)
    private BookingStatus status;

    public Booking() {}

    public Booking(String json){
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.readValue(json, Booking.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON format", e);
        }
    }

    public Booking(long uniqueBookingCode, Flight flight, ClassOfService classOfService, List<Passenger> booking_passenger, ZonedDateTime creationDate, ZonedDateTime confirmationDate, ZonedDateTime cancellationDate, BookingStatus status) {
        this.uniqueBookingCode = uniqueBookingCode;
        this.flight = flight;
        this.classOfService = classOfService;
        this.booking_passenger = booking_passenger;
        this.creationDate = creationDate;
        this.confirmationDate = confirmationDate;
        this.cancellationDate = cancellationDate;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUniqueBookingCode() {
        return uniqueBookingCode;
    }

    public void setUniqueBookingCode(long uniqueBookingCode) {
        this.uniqueBookingCode = uniqueBookingCode;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public ClassOfService getClassOfService() {
        return classOfService;
    }

    public void setClassOfService(ClassOfService classOfService) {
        this.classOfService = classOfService;
    }

    public List<Passenger> getBooking_passenger() {
        return booking_passenger;
    }

    public void setBooking_passenger(List<Passenger> booking_passenger) {
        this.booking_passenger = booking_passenger;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ZonedDateTime getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(ZonedDateTime confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public ZonedDateTime getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(ZonedDateTime cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}
