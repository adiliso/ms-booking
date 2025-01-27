package az.edu.turing.booking.service;

import az.edu.turing.booking.model.dto.BookingDto;
import az.edu.turing.booking.model.dto.requests.BookingCreateRequest;
import az.edu.turing.booking.model.dto.requests.BookingUpdateRequest;
import az.edu.turing.booking.model.enums.BookingStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Transactional(readOnly = true)
public interface BookingService {

    @Transactional
    BookingDto create(long createdBy, BookingCreateRequest request);

    @Transactional
    BookingDto update(long updatedBy, long bookingId, BookingUpdateRequest request);

    Collection<BookingDto> getBookingsByUsername(String username);

    BookingDto getBookingById(long id);

    @Transactional
    BookingDto updateStatus(long userId, long bookingId, BookingStatus status);

    @Transactional
    void cancel(long id);
}
