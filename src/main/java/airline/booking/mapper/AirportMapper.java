package airline.booking.mapper;

import airline.booking.dto.request.AirportRequest;
import airline.booking.dto.response.AirportResponse;
import airline.booking.model.Airport;
import org.springframework.stereotype.Component;

@Component
public class AirportMapper {

    public Airport toEntity(AirportRequest request) {
        Airport airport = new Airport();
        airport.setIataCode(request.getIataCode());
        airport.setName(request.getName());
        airport.setCity(request.getCity());
        return airport;
    }

    public AirportResponse toResponse(Airport airport) {
        AirportResponse response = new AirportResponse();
        response.setId(airport.getId());
        response.setIataCode(airport.getIataCode());
        response.setName(airport.getName());
        response.setCity(airport.getCity());
        return response;
    }
}