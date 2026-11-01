package airline.booking.controller;

import airline.booking.dto.response.AirportResponse;
import airline.booking.service.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/airport")
@RequiredArgsConstructor
public class AirportController {

    private final AirportService airportService;

    @GetMapping("/all")
    public List<AirportResponse> getAll() { return airportService.getAll(); }
}
