package airline.booking;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public class Passenger {
    private String fullName;
    private LocalDate birthday;
    private String documents;

    public Passenger(String fullName, LocalDate birthday, String documents) {}
    public Passenger() {}

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }
}
