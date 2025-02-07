package az.edu.turing.booking.service.impl;

import az.edu.turing.booking.domain.entity.BookingEntity;
import az.edu.turing.booking.domain.repository.BookingRepository;
import az.edu.turing.booking.exception.BaseException;
import az.edu.turing.booking.mapper.BookingMapper;
import az.edu.turing.booking.model.dto.BookingDto;
import az.edu.turing.booking.model.dto.request.BookingCreateRequest;
import az.edu.turing.booking.model.dto.request.BookingUpdateRequest;
import az.edu.turing.booking.model.dto.response.FlightDetailsResponse;
import az.edu.turing.booking.model.dto.response.PageResponse;
import az.edu.turing.booking.model.enums.BookingStatus;
import az.edu.turing.booking.service.BookingService;
import az.edu.turing.booking.service.FlightService;
import az.edu.turing.booking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
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

        checkDepartureAvailability(flight);

        checkExistenceOfFreeSeats(request, flight);

        BookingEntity booking = bookingMapper.toEntity(userId, request);
        booking.setTotalPrice(flight.getPrice() * request.getNumberOfPassengers());

        request.getUsernameOfPassengers()
                .forEach(p -> booking.addUser(userService.findByUsername(p)));

        flightService.releaseSeats(flight.getFlightId(), request.getNumberOfPassengers());

        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    private static void checkExistenceOfFreeSeats(BookingCreateRequest request, FlightDetailsResponse flight) {
        if (flight.getFreeSeats() < request.getNumberOfPassengers()) {
            throw new BaseException(INVALID_OPERATION, String.format("There are only %d free seats in this flight",
                    flight.getFreeSeats()));
        }
    }

    private static void checkDepartureAvailability(FlightDetailsResponse flight) {
        if (flight.getDepartureTime().isBefore(LocalDateTime.now().plusMinutes(30))) {
            throw new BaseException(INVALID_OPERATION, "Too late for booking");
        }
    }

    @Transactional
    @Override
    public BookingDto update(Long userId, Long bookingId, BookingUpdateRequest request) {
        userExistsById(userId);

        BookingEntity booking = findById(bookingId);

        bookingMapper.updateEntity(booking, userId, request);

        return bookingMapper.toDto(booking);
    }

    @Override
    public PageResponse<BookingDto> getBookingsByUsername(String username, final int pageNumber, final int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        var responses = bookingRepository.findAllByUsersUsername(username, pageable)
                .map(bookingMapper::toDto);

        return PageResponse.of(responses.getContent(),
                pageNumber,
                pageSize,
                responses.getTotalElements(),
                responses.getTotalPages());
    }

    @Override
    public BookingDto getBookingById(Long id) {
        return bookingMapper.toDto(findById(id));
    }

    @Transactional
    @Override
    public BookingDto updateStatus(Long userId, Long id, BookingStatus status) {
        if (!bookingRepository.existsByIdAndCreatedByIs(id, userId)) {
            throw new BaseException(ACCESS_DENIED);
        }

        BookingEntity booking = findById(id);
        booking.setStatus(status);
        booking.setUpdatedBy(userId);

        return bookingMapper.toDto(bookingRepository.save(booking));
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
