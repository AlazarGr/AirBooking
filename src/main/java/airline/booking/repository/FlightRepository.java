package airline.booking.repository;

import airline.booking.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    boolean existsByFlightNumberAndDepartureDateTime(String flightNumber, ZonedDateTime departureDateTime);
}
