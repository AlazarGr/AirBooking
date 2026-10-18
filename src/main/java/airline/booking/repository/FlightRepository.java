package airline.booking.repository;

import airline.booking.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    boolean existsByFlightNumberAndDepartureDateTime(String flightNumber, ZonedDateTime departureDateTime);

    @Query("""
    select f from Flight f
    where f.departureAirport.iataCode = :from
      and f.arrivalAirport.iataCode = :to
      and f.departureDateTime >= :start
      and f.departureDateTime < :end
    order by f.departureDateTime
    """)
    List<Flight> search(@Param("from") String from,
                        @Param("to") String to,
                        @Param("start") ZonedDateTime start,
                        @Param("end") ZonedDateTime end);
}
