package airline.booking.model;

import airline.booking.enums.BookingStatus;
import airline.booking.enums.ClassOfService;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "booking_code", nullable = false, unique = true)
    private String bookingCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private AppUser owner;

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
}
