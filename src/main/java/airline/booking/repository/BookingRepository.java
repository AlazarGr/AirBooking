package airline.booking.repository;

import airline.booking.dto.response.BookingResponse;
import airline.booking.enums.ClassOfService;
import airline.booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByBookingCode(String bookingCode);

    boolean existsByBookingCode(String bookingCode);

    @Query("""
    SELECT COUNT(p)
    FROM Booking b
    JOIN b.passengers p
    WHERE b.flight.id = :flightId
        AND b.classOfService = :classOfService
        AND b.status IN (
            airline.booking.enums.BookingStatus.CREATED,
            airline.booking.enums.BookingStatus.CONFIRMED
        )
    """)
    long countActivePassengersByFlightAndClass(
            @Param("flightId") Long flightId,
            @Param("classOfService") ClassOfService classOfService
    );


    @Query("""
    SELECT COUNT(b)
    FROM Booking b
    JOIN b.passengers p
    WHERE b.flight.id = :flightId
      AND p.documentsNumber = :documentsNumber
      AND b.status IN (
          airline.booking.enums.BookingStatus.CREATED,
          airline.booking.enums.BookingStatus.CONFIRMED
      )
    """)
    long countActiveBookingsForPassengerAndFlight(
            @Param("documentsNumber") String documentNumber,
            @Param("flightId") Long flightId
    );
}
