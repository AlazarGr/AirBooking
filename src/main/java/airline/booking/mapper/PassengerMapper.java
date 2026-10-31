package airline.booking.mapper;

import airline.booking.dto.request.PassengerRequest;
import airline.booking.dto.response.PassengerResponse;
import airline.booking.model.Passenger;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PassengerMapper {
    public PassengerMapper() {}


    // Request -> Entity
    public Passenger toEntity(PassengerRequest passengerRequest) {
        Passenger passenger = new Passenger();
        passenger.setFullName(passengerRequest.getFullName());
        passenger.setBirthDay(passengerRequest.getBirthDate());
        passenger.setDocumentNumber(passengerRequest.getDocumentNumber());
        return passenger;
    }

    // Entity -> Response
    public PassengerResponse toResponse(Passenger passenger) {
        PassengerResponse passengerResponse = new PassengerResponse();
        passengerResponse.setFullName(passenger.getFullName());
        passengerResponse.setBirthDate(passenger.getBirthDay());
        passengerResponse.setDocumentNumber(passenger.getDocumentNumber());
        return passengerResponse;
    }

    public Function<? super Passenger,?> toPassengerResponse;

    public Function<? super PassengerRequest,?> toPassengerEntity;

}
