package az.edu.turing.booking.exception;

import az.edu.turing.booking.model.constant.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<GlobalErrorResponse> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).
                body(GlobalErrorResponse.builder()
                        .errorCode(ErrorCode.NOT_FOUND)
                        .errorMessage(e.getMessage())
                        .timestamp(LocalDateTime.now())
                        .requestId(UUID.randomUUID())
                        .build());
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<GlobalErrorResponse> handleAlreadyExistsException(AlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                body(GlobalErrorResponse.builder()
                        .errorCode(ErrorCode.ALREADY_EXISTS)
                        .errorMessage(e.getMessage())
                        .timestamp(LocalDateTime.now())
                        .requestId(UUID.randomUUID())
                        .build());
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<GlobalErrorResponse> handleInvalidOperationException(InvalidOperationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                body(GlobalErrorResponse.builder()
                        .errorCode(ErrorCode.INVALID_OPERATION)
                        .errorMessage(e.getMessage())
                        .timestamp(LocalDateTime.now())
                        .requestId(UUID.randomUUID())
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<GlobalErrorResponse> handleAccessDeniedExceptionException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).
                body(GlobalErrorResponse.builder()
                        .errorCode(ErrorCode.ACCESS_DENIED)
                        .errorMessage(e.getMessage())
                        .timestamp(LocalDateTime.now())
                        .requestId(UUID.randomUUID())
                        .build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<GlobalErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GlobalErrorResponse.builder()
                        .errorCode(ErrorCode.CONSTRAINT_VIOLATION)
                        .errorMessage(e.getMessage())
                        .timestamp(LocalDateTime.now())
                        .requestId(UUID.randomUUID())
                        .build());
    }
}
