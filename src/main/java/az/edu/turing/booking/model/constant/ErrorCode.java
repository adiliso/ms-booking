package az.edu.turing.booking.model.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorCode {

    public static final String NOT_FOUND = "not_found";
    public static final String ACCESS_DENIED = "access_denied";
    public static final String ALREADY_EXISTS = "already_exists";
    public static final String INVALID_OPERATION = "invalid_operation";
    public static final String INVALID_INPUT = "invalid_input";
    public static final String CONSTRAINT_VIOLATION = "constraint_violation";
}
