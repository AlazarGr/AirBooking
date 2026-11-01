package airline.booking.service;

import airline.booking.dto.response.AirportResponse;
import airline.booking.exception.EntityNotFoundException;
import airline.booking.mapper.AirportMapper;
import airline.booking.model.Airport;
import airline.booking.repository.AirportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirportService {

    private final AirportRepository airportRepository;
    private final AirportMapper airportMapper;

    public Airport getByIataCode(String iataCode) {
        return airportRepository.findByIataCode(iataCode)
                .orElseThrow(() -> new EntityNotFoundException("Airport", "кодом", iataCode));
    }
    public List<AirportResponse> getAll() {
        List<Airport> airports = airportRepository.findAll();
        return airports.stream().map(airportMapper::toResponse).toList();
    }
}