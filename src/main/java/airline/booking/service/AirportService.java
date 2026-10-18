package airline.booking.service;

import airline.booking.exception.EntityNotFoundException;
import airline.booking.model.Airport;
import airline.booking.repository.AirportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//public class AirportService {
//    @Autowired
//    private final AirportRepository airportRepository;
//
//    public AirportService(AirportRepository airportRepository) {
//        this.airportRepository = airportRepository;
//    }
//
//    public Airport getByIataCode(String IataCode) {
//        return airportRepository.getByIataCode(IataCode);
//    }
//}

@Service
public class AirportService {

    private final AirportRepository airportRepository; // экземпляр, его внедрит Spring

    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }
    public Airport getByIataCode(String iataCode) {
        return airportRepository.findByIataCode(iataCode)
                .orElseThrow(() -> new EntityNotFoundException("Airport", "кодом", iataCode));
    }
    public List<Airport> getAllAirports() {
        return airportRepository.findAll();
    }
}