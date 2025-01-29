package az.edu.turing.booking.controller;


import az.edu.turing.booking.model.dto.BookingDto;
import az.edu.turing.booking.model.dto.requests.BookingCreateRequest;
import az.edu.turing.booking.model.dto.requests.BookingUpdateRequest;
import az.edu.turing.booking.model.enums.BookingStatus;
import az.edu.turing.booking.service.impl.BookingServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/booking")
public class BookingController {
    private final BookingServiceImpl bookingService;



    @PostMapping
    public ResponseEntity<BookingDto> createBooking(
            @RequestHeader("X-User-Id") Long createdBy,
           @Valid @RequestBody BookingCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.create(createdBy, request));
    }

    @PutMapping("/{bookingId}")
    public ResponseEntity<BookingDto> updateBookingById(
            @RequestHeader("X-User-Id") Long updatedBy,
            @PathVariable Long bookingId,
            @Valid @RequestBody BookingUpdateRequest request) {
        return ResponseEntity.ok(bookingService.update(updatedBy, bookingId, request));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<Collection<BookingDto>> getBookingsByUsername(@PathVariable String username) {
        return ResponseEntity.ok(bookingService.getBookingsByUsername(username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BookingDto> updateBookingStatus(
            @RequestHeader("X-User-Id") Long updatedBy,
            @PathVariable Long id,
            @RequestParam @NotNull BookingStatus status) {
        return ResponseEntity.ok(bookingService.updateStatus(updatedBy, id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}
