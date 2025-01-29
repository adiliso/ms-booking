package az.edu.turing.booking.service;

import az.edu.turing.booking.model.dto.BookingDto;
import az.edu.turing.booking.model.dto.request.BookingCreateRequest;
import az.edu.turing.booking.model.dto.request.BookingUpdateRequest;
import az.edu.turing.booking.model.enums.BookingStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Transactional(readOnly = true)
public interface BookingService {

    @Transactional
    BookingDto create(Long createdBy, BookingCreateRequest request);

    @Transactional
    BookingDto update(Long updatedBy, Long bookingId, BookingUpdateRequest request);

    Collection<BookingDto> getBookingsByUsername(String username);

    BookingDto getBookingById(Long id);

    @Transactional
    BookingDto updateStatus(Long updatedBy, Long bookingId, BookingStatus status);

    @Transactional
    void cancel(Long id);
}
