package airline.booking.controller;

import airline.booking.dto.request.FlightRequest;
import airline.booking.dto.response.FlightResponse;
import airline.booking.service.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;


    // создание нового рейса
    @PostMapping("/flights")
    @ResponseStatus(HttpStatus.CREATED)
    public FlightResponse createFlight(@Valid @RequestBody FlightRequest request) {
        return flightService.createFlight(request);
    }

    //получение рейса по ID
    @GetMapping("/flights/{id}")
    public FlightResponse getFlight(@PathVariable("id") Long id) {
        return flightService.getFlightById(id);
    }

    // Список всех рейсов с даты по дату
    @GetMapping("/flights")
    public List<FlightResponse> searchFlights(
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return flightService.searchFlights(from, to, date);
    }

    // Отмена рейса
    @PostMapping("/flights/{id}/cancel")
    public FlightResponse cancelFlight(@PathVariable("id") Long id) {
        return flightService.cancelFlight(id);
    }
}