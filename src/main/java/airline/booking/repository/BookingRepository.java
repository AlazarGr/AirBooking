package airline.booking.repository;

import airline.booking.enums.ClassOfService;
import airline.booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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
      AND p.documentNumber = :documentNumber
      AND b.status IN (
          airline.booking.enums.BookingStatus.CREATED,
          airline.booking.enums.BookingStatus.CONFIRMED
      )
    """)
    long countActiveForPassengerAndFlight(
            @Param("documentNumber") String documentNumber,
            @Param("flightId") Long flightId
    );

    boolean existsByFlightId(Long flightId);

    List<Booking> findByFlightId(Long flightId);

    @Query("""
    select count(b)
    from Booking b join b.passengers p
    where b.flight.id = :flightId
      and p.documentNumber = :documentNumber
      and b.id <> :bookingId
      and b.status in (airline.booking.enums.BookingStatus.CREATED,
                       airline.booking.enums.BookingStatus.CONFIRMED)
    """)
    long countActiveForPassengerAndFlightExcluding(@Param("documentNumber") String documentNumber,
                                                   @Param("flightId") Long flightId,
                                                   @Param("bookingId") Long bookingId);


    Optional<Booking> findByIdAndOwnerUsername(Long id, String username);
    Optional<Booking> findByBookingCodeAndOwnerUsername(String bookingCode, String username);
    List<Booking> findByOwnerUsername(String username);
    List<Booking> findByOwnerUsernameAndFlightId(String username, Long flightId);
}
