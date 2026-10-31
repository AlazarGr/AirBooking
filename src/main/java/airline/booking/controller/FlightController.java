package airline.booking.controller;

import airline.booking.dto.request.FlightRequest;
import airline.booking.dto.response.FlightResponse;
import airline.booking.service.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FlightResponse createFlight(@Valid @RequestBody FlightRequest request) {
        return flightService.createFlight(request);
    }

    @GetMapping("/{id}")
    public FlightResponse getFlight(@PathVariable("id") Long id) {
        log.info("getFlight id={}", id);
        return flightService.getFlightById(id);
    }

    // GET /flights?from=OMS&to=SVO&date=2026-10-20  (+ необязательный arrivalDate)
    @GetMapping
    public List<FlightResponse> searchFlights(
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam(value = "arrivalDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate arrivalDate) {
        return flightService.searchFlights(from, to, departureDate, arrivalDate);
    }

    @GetMapping("/all")
    public List<FlightResponse> getAllFlights() {
        return flightService.getAllFlights();
    }

    @PutMapping("/{id}")
    public FlightResponse updateFlight(@PathVariable("id") Long id,
                                       @Valid @RequestBody FlightRequest request) {
        return flightService.updateFlight(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFlight(@PathVariable("id") Long id) {
        flightService.deleteFlight(id);
    }

    @PostMapping("/{id}/cancel")
    public FlightResponse cancelFlight(@PathVariable("id") Long id) {
        return flightService.cancelFlight(id);
    }
}