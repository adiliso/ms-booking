package az.edu.turing.booking.exception;

import az.edu.turing.booking.model.enums.ErrorEnum;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final Integer code;
    private final String message;

    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseException(ErrorEnum errorEnum) {
        this(errorEnum.getCode(), errorEnum.getMessage());
    }

    public BaseException(ErrorEnum errorEnum, String message) {
        this(errorEnum.getCode(), message);
    }
}
