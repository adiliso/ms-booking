package az.edu.turing.booking.service.impl;

import az.edu.turing.booking.domain.entity.BookingEntity;
import az.edu.turing.booking.domain.entity.FlightEntity;
import az.edu.turing.booking.domain.entity.UserEntity;
import az.edu.turing.booking.domain.repository.BookingRepository;
import az.edu.turing.booking.domain.repository.FlightRepository;
import az.edu.turing.booking.domain.repository.UserRepository;
import az.edu.turing.booking.exception.BadRequestException;
import az.edu.turing.booking.exception.NotFoundException;
import az.edu.turing.booking.mapper.BookingMapper;
import az.edu.turing.booking.model.dto.BookingDto;
import az.edu.turing.booking.model.dto.requests.BookingCreateRequest;
import az.edu.turing.booking.model.dto.requests.BookingUpdateRequest;
import az.edu.turing.booking.model.enums.BookingStatus;
import az.edu.turing.booking.model.enums.UserRole;
import az.edu.turing.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final FlightRepository flightRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto create(long createdBy, BookingCreateRequest request) {
        userExistsById(createdBy);

        FlightEntity flight = flightRepository
                .findById(request.getFlightId())
                .orElseThrow(() -> new NotFoundException("Flight not found with id: " + request.getFlightId()));

        if (flight.getDepartureTime().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new BadRequestException("Too late booking for flight");
        }

        if (flight.getFlightDetails().getFreeSeats() < request.getNumberOfPassengers()) {
            throw new BadRequestException(String.format("There are only %d free seats in this flight",
                    flight.getFlightDetails().getFreeSeats()));
        }

        BookingEntity booking = bookingMapper.toEntity(createdBy, request);

        request.getPassengers()
                .forEach(p -> booking.addPassenger(userRepository.findByUsername(p)
                        .orElseThrow(() -> new NotFoundException("User not found with username: " + p))));

        bookingRepository.save(booking);

        return bookingMapper.toDto(booking);
    }

    @Override
    public BookingDto update(long updatedBy, long bookingId, BookingUpdateRequest request) {
        userExistsById(updatedBy);

        BookingEntity booking = findById(bookingId);

        booking.setTotalPrice(request.getTotalPrice());
        booking.setStatus(request.getStatus());

        bookingRepository.save(booking);

        return bookingMapper.toDto(booking);
    }

    @Override
    public Collection<BookingDto> getBookingsByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username))
                .getBookings()
                .stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public BookingDto getBookingById(long id) {
        return bookingMapper.toDto(findById(id));
    }

    @Override
    public BookingDto updateStatus(long userId, long id, BookingStatus status) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        if (!user.getRole().equals(UserRole.ADMIN)) {
            throw new BadRequestException((String.format("User %s is not admin", userId)));
        }

        BookingEntity booking = findById(id);
        booking.setStatus(status);

        bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    @Override
    public void cancel(long id) {
        BookingEntity booking = findById(id);
        booking.setStatus(BookingStatus.CANCELLED);

        bookingRepository.save(booking);
    }

    private void userExistsById(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with id " + userId);
        }
    }

    private BookingEntity findById(long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found with id: " + id));
    }
}
