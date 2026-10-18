package airline.booking.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entityName, String identifierLabel, Object identifierValue) {
        super(String.format("%s с %s %s не найден", entityName, identifierLabel, identifierValue));
    }
}