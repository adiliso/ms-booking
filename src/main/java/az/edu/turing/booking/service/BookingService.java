package az.edu.turing.booking.service;

import az.edu.turing.booking.model.dto.BookingDto;
import az.edu.turing.booking.model.dto.request.BookingCreateRequest;
import az.edu.turing.booking.model.dto.request.BookingUpdateRequest;
import az.edu.turing.booking.model.dto.response.PageResponse;
import az.edu.turing.booking.model.enums.BookingStatus;

import java.util.Collection;

public interface BookingService {

    BookingDto create(Long userId, BookingCreateRequest request);

    BookingDto update(Long userId, Long bookingId, BookingUpdateRequest request);

    PageResponse<BookingDto> getBookingsByUsername(String username, final int pageNumber, final int pageSize);

    BookingDto getBookingById(Long id);

    BookingDto updateStatus(Long userId, Long bookingId, BookingStatus status);

    default void cancel(Long userId, Long id){
        updateStatus(userId, id, BookingStatus.CANCELLED);
    }
}
