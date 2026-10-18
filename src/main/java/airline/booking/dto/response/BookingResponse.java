package airline.booking.dto.response;

import airline.booking.enums.BookingStatus;
import airline.booking.enums.ClassOfService;

import java.time.ZonedDateTime;
import java.util.List;

public class BookingResponse {

    private Long id;
    private String bookingCode;
    private Long flightId;
    private String flightNumber;        // немного контекста о рейсе, чтобы не заставлять клиента делать второй запрос
    private ClassOfService classOfService;
    private List<PassengerResponse> passengers;
    private BookingStatus status;
    private ZonedDateTime creationDate;
    private ZonedDateTime confirmationDate;
    private ZonedDateTime cancellationDate;




    // геттеры/сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public ClassOfService getClassOfService() {
        return classOfService;
    }

    public void setClassOfService(ClassOfService classOfService) {
        this.classOfService = classOfService;
    }

    public List<PassengerResponse> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerResponse> passengers) {
        this.passengers = passengers;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ZonedDateTime getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(ZonedDateTime confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public ZonedDateTime getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(ZonedDateTime cancellationDate) {
        this.cancellationDate = cancellationDate;
    }



}