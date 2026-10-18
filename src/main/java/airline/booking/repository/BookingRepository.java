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

//    @Query("""
//        select count(b)
//        from Booking b join b.passengers p
//        where b.flight.id = :flightId
//          and b.classOfService = :classOfService
//          and b.status in (airline.booking.enums.BookingStatus.CREATED,
//                           airline.booking.enums.BookingStatus.CONFIRMED)
//        """)
//    int countActivePassengersByFlightAndClass(@Param("flightId") Long flightId,
//                                               @Param("classOfService") ClassOfService classOfService);

    @Query("""
    SELECT COUNT(b)
    FROM Booking b
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
//    @Query("""
//        select count(b) > 0
//        from Booking b join b.passengers p
//        where b.flight.id = :flightId
//          and p.documentNumber = :documentNumber
//          and b.status in (airline.booking.enums.BookingStatus.CREATED,
//                           airline.booking.enums.BookingStatus.CONFIRMED)
//        """)
//    boolean existsActiveBookingForPassengerAndFlight(@Param("documentNumber") String documentNumber,
//                                                     @Param("flightId") Long flightId);

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
