package airline.booking.dto.request;

import airline.booking.enums.ClassOfService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class BookingRequest {
    private Long flightId;
    private ClassOfService classOfService;
    private List<PassengerRequest> passengers;

    // геттеры/сеттеры


    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public ClassOfService getClassOfService() {
        return classOfService;
    }

    public void setClassOfService(ClassOfService classOfService) {
        this.classOfService = classOfService;
    }

    public List<PassengerRequest> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerRequest> passengers) {
        this.passengers = passengers;
    }
}