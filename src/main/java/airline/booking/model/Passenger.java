package airline.booking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Embeddable
public class Passenger {

    @NotBlank
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDay;

    @NotBlank
    @Column(name = "document_number", nullable = false)
    private String documentsNumber;

    public Passenger(String fullName, LocalDate birthDay, String documents) {}
    public Passenger() {}

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getBirthday() {
        return birthDay;
    }

    public void setBirthday(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public String getDocumentsNumber() {
        return documentsNumber;
    }

    public void setDocumentsNumber(String documentsNumber) {
        this.documentsNumber = documentsNumber;
    }
}
