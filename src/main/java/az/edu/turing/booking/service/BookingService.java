package az.edu.turing.booking.service;

import az.edu.turing.booking.domain.entity.BookingEntity;
import az.edu.turing.booking.model.enums.BookingStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Transactional(readOnly = true)
public interface BookingService {

    BookingEntity create(BookingEntity bookingEntity);

    BookingEntity update(long id, BookingEntity bookingEntity);

    Collection<BookingEntity> getFlightsByUsername(String username);

    BookingEntity getBookingById(long id);

    BookingEntity updateStatus(long id, BookingStatus status);

    void cancel(long id);
}
