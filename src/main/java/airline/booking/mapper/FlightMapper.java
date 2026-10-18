package airline.booking.mapper;

import airline.booking.dto.request.FlightRequest;
import airline.booking.dto.response.FlightResponse;
import airline.booking.model.Airport;
import airline.booking.model.Flight;
import org.springframework.stereotype.Component;

@Component
public class FlightMapper {

    // Request -> Entity. Аэропорты уже найдены сервисом по IATA-коду.
    public Flight toEntity(FlightRequest request, Airport departureAirport, Airport arrivalAirport) {
        Flight flight = new Flight();
        flight.setFlightNumber(request.getFlightNumber());
        flight.setDepartureAirport(departureAirport);
        flight.setArrivalAirport(arrivalAirport);
        flight.setDepartureDateTime(request.getDepartureDateTime());
        flight.setArrivalDateTime(request.getArrivalDateTime());
        flight.setEconomySeats(request.getEconomySeats());
        flight.setBusinessSeats(request.getBusinessSeats());
        return flight;
        // status сервис проставит сам (всегда SCHEDULED при создании)
    }

    // Entity -> Response. Доступные места уже посчитаны сервисом.
    public FlightResponse toResponse(Flight flight, int availableEconomySeats, int availableBusinessSeats) {
        FlightResponse response = new FlightResponse();
        response.setId(flight.getId());
        response.setFlightNumber(flight.getFlightNumber());
        response.setDepartureAirportIataCode(flight.getDepartureAirport().getIataCode());
        response.setArrivalAirportIataCode(flight.getArrivalAirport().getIataCode());
        response.setDepartureDateTime(flight.getDepartureDateTime());
        response.setArrivalDateTime(flight.getArrivalDateTime());
        response.setEconomySeats(flight.getEconomySeats());
        response.setBusinessSeats(flight.getBusinessSeats());
        response.setAvailableEconomySeats(availableEconomySeats);
        response.setAvailableBusinessSeats(availableBusinessSeats);
        response.setStatus(flight.getStatus());
        return response;
    }
}