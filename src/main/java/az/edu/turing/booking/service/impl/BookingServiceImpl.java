package az.edu.turing.booking.service.impl;

import az.edu.turing.booking.domain.entity.BookingEntity;
import az.edu.turing.booking.domain.repository.BookingRepository;
import az.edu.turing.booking.exception.BaseException;
import az.edu.turing.booking.mapper.BookingMapper;
import az.edu.turing.booking.model.dto.BookingDto;
import az.edu.turing.booking.model.dto.request.BookingCreateRequest;
import az.edu.turing.booking.model.dto.request.BookingUpdateRequest;
import az.edu.turing.booking.model.dto.response.FlightDetailsResponse;
import az.edu.turing.booking.model.enums.BookingStatus;
import az.edu.turing.booking.service.BookingService;
import az.edu.turing.booking.service.FlightService;
import az.edu.turing.booking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

import static az.edu.turing.booking.model.enums.ErrorEnum.ACCESS_DENIED;
import static az.edu.turing.booking.model.enums.ErrorEnum.BOOKING_NOT_FOUND;
import static az.edu.turing.booking.model.enums.ErrorEnum.INVALID_OPERATION;
import static az.edu.turing.booking.model.enums.ErrorEnum.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final FlightService flightService;
    private final UserService userService;

    @Transactional
    @Override
    public BookingDto create(Long userId, BookingCreateRequest request) {
        userExistsById(userId);

        FlightDetailsResponse flight = flightService.getInfoById(request.getFlightId());

        if (flight.getDepartureTime().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new BaseException(INVALID_OPERATION, "Too late for booking");
        }

        if (flight.getFreeSeats() < request.getNumberOfPassengers()) {
            throw new BaseException(INVALID_OPERATION, String.format("There are only %d free seats in this flight",
                    flight.getFreeSeats()));
        }

        BookingEntity booking = bookingMapper.toEntity(userId, request);
        booking.setTotalPrice(flight.getPrice() * request.getNumberOfPassengers());

        request.getUsernameOfPassengers()
                .forEach(p -> booking.addUser(userService.findByUsername(p)));

        flightService.releaseSeats(flight.getFlightId(), request.getNumberOfPassengers());

        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Transactional
    @Override
    public BookingDto update(Long userId, Long bookingId, BookingUpdateRequest request) {
        userExistsById(userId);

        BookingEntity booking = findById(bookingId);

        bookingMapper.updateEntity(booking, userId, request);

        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public Collection<BookingDto> getBookingsByUsername(String username) {
        return userService.findByUsername(username)
                .getBookings()
                .stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public BookingDto getBookingById(Long id) {
        return bookingMapper.toDto(findById(id));
    }

    @Transactional
    @Override
    public BookingDto updateStatus(Long userId, Long id, BookingStatus status) {
        if (!userService.isAdmin(id)) {
            throw new BaseException(ACCESS_DENIED);
        }

        BookingEntity booking = findById(id);
        booking.setStatus(status);
        booking.setUpdatedBy(userId);

        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Transactional
    @Override
    public void cancel(Long id) {
        BookingEntity booking = findById(id);
        booking.setStatus(BookingStatus.CANCELLED);

        flightService.addSeats(booking.getFlight().getId(), booking.getUsers().size());

        bookingRepository.save(booking);
    }

    private void userExistsById(Long userId) {
        if (!userService.existsById(userId)) {
            throw new BaseException(USER_NOT_FOUND);
        }
    }

    private BookingEntity findById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new BaseException(BOOKING_NOT_FOUND));
    }
}
