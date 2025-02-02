package az.edu.turing.booking.service;

import az.edu.turing.booking.model.dto.BookingDto;
import az.edu.turing.booking.model.dto.request.BookingCreateRequest;
import az.edu.turing.booking.model.dto.request.BookingUpdateRequest;
import az.edu.turing.booking.model.enums.BookingStatus;

import java.util.Collection;

public interface BookingService {

    BookingDto create(Long userId, BookingCreateRequest request);

    BookingDto update(Long userId, Long bookingId, BookingUpdateRequest request);

    Collection<BookingDto> getBookingsByUsername(String username);

    BookingDto getBookingById(Long id);

    BookingDto updateStatus(Long userId, Long bookingId, BookingStatus status);

    void cancel(Long id);
}
