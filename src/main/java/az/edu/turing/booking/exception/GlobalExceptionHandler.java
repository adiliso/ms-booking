package az.edu.turing.booking.exception;

import az.edu.turing.booking.model.dto.response.GlobalErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<GlobalErrorResponse> handleBaseException(BaseException ex) {
        addErrorLog(HttpStatus.resolve(ex.getCode()),
                ex.getMessage(), ex.getClass().getSimpleName());
        return ResponseEntity.status(Optional
                        .ofNullable(HttpStatus.resolve(ex.getCode()))
                        .orElse(HttpStatus.INTERNAL_SERVER_ERROR))
                .body(GlobalErrorResponse.builder()
                        .errorCode(ex.getCode())
                        .errorMessage(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .requestId(UUID.randomUUID())
                        .build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<GlobalErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        addErrorLog(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GlobalErrorResponse.builder()
                        .errorCode(400)
                        .errorMessage(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .requestId(UUID.randomUUID())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        addErrorLog(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GlobalErrorResponse.builder()
                        .errorCode(400)
                        .errorMessage(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .requestId(UUID.randomUUID())
                        .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GlobalErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        addErrorLog(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GlobalErrorResponse.builder()
                        .errorCode(400)
                        .errorMessage(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .requestId(UUID.randomUUID())
                        .build());
    }

    protected void addErrorLog(HttpStatus httpStatus, String errorMessage, String exceptionType) {
        int statusCode = (httpStatus != null) ? httpStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR.value();
        log.error("HTTP Status: {} | Error Message: {} | Exception Type: {}", statusCode, errorMessage, exceptionType);
    }
}
