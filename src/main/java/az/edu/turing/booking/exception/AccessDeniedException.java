package az.edu.turing.booking.exception;

public class AccessDeniedException extends RuntimeException{

    public AccessDeniedException(String message) {
        super(message);
    }
}
