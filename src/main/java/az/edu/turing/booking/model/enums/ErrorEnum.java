package az.edu.turing.booking.model.enums;

import lombok.Getter;

@Getter
public enum ErrorEnum {

    ACCESS_DENIED(403, "Access Denied"),
    USER_ALREADY_EXISTS(400, "User already exists"),
    USER_NOT_FOUND(404, "User not found"),
    BOOKING_NOT_FOUND(404, "Booking not found"),
    FLIGHT_NOT_FOUND(404, "Flight not found"),
    FLIGHT_DETAILS_NOT_FOUND(404, "Flight details not found"),
    INVALID_OPERATION(405, "Invalid operation"),
    PASSWORDS_DONT_MATCH(422, "Passwords do not match");

    private final Integer code;
    private final String message;

    ErrorEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
