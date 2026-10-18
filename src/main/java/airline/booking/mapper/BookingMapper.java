package airline.booking.mapper;

import airline.booking.dto.request.BookingRequest;
import airline.booking.dto.request.PassengerRequest;
import airline.booking.dto.response.BookingResponse;
import airline.booking.dto.response.PassengerResponse;
import airline.booking.enums.BookingStatus;
import airline.booking.model.Booking;
import airline.booking.model.Flight;
import airline.booking.model.Passenger;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class BookingMapper {

    private final PassengerMapper passengerMapper;

    public BookingMapper(PassengerMapper passengerMapper) {
        this.passengerMapper = passengerMapper;
    }

    // Request -> Entity
    public Booking toEntity(BookingRequest request, Flight flight) {
        Booking booking = new Booking();
        booking.setFlight(flight);
        booking.setClassOfService(request.getClassOfService());
        booking.setPassengers(
                request.getPassengers().stream()
                        .map(passengerMapper::toEntity)
                        .toList()
        );
        return booking;
        // status, bookingCode, creationDate — сервис ДОЛЖЕН ставить сам! Не забудь.
    }


    // Entity -> Response
    public BookingResponse toResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setBookingCode(booking.getBookingCode());
        response.setFlightId(booking.getFlight().getId());
        response.setFlightNumber(booking.getFlight().getFlightNumber());
        response.setClassOfService(booking.getClassOfService());
        response.setPassengers(
                booking.getPassengers().stream()
                        .map(passengerMapper::toResponse)
                        .toList()
        );
        response.setStatus(booking.getStatus());
        response.setCreationDate(booking.getCreationDate());
        response.setConfirmationDate(booking.getConfirmationDate());
        response.setCancellationDate(booking.getCancellationDate());
        return response;
    }
}

