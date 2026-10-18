package airline.booking.model;

import airline.booking.enums.BookingStatus;
import airline.booking.enums.ClassOfService;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "booking_code", nullable = false, unique = true)
    private String bookingCode;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "class_of_service", nullable = false)
    private ClassOfService classOfService;

    @ElementCollection 
    @CollectionTable(
            name = "booking_passenger", 
            joinColumns = @JoinColumn(name = "booking_id") 
    )
    @Column(nullable = false)
    @NotEmpty
    @Size(min = 1, max = 5, message = "От 1 до 5 пассажиров в одном бронировании")
    private List<@Valid Passenger> passengers;

    @Column(name = "creation_date", nullable = false)
    private ZonedDateTime creationDate;

    @Column(name = "confirmation_date")
    private ZonedDateTime confirmationDate = null;

    @Column(name = "cancellation_date")
    private ZonedDateTime cancellationDate = null;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    public Booking() {}

    public Booking(String bookingCode, Flight flight, ClassOfService classOfService, List<Passenger> passengers, ZonedDateTime creationDate, ZonedDateTime confirmationDate, ZonedDateTime cancellationDate, BookingStatus status) {
        this.bookingCode = bookingCode;
        this.flight = flight;
        this.classOfService = classOfService;
        this.passengers = passengers;
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

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(@NotNull String bookingCode) {
        this.bookingCode = bookingCode;
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

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
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
