package az.edu.turing.booking.service;

import az.edu.turing.booking.domain.entity.BookingEntity;
import az.edu.turing.booking.domain.entity.FlightEntity;
import az.edu.turing.booking.domain.entity.UserEntity;
import az.edu.turing.booking.domain.repository.BookingRepository;
import az.edu.turing.booking.exception.BaseException;
import az.edu.turing.booking.mapper.BookingMapper;
import az.edu.turing.booking.model.dto.BookingDto;
import az.edu.turing.booking.model.enums.BookingStatus;
import az.edu.turing.booking.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private FlightService flightService;

    @Mock
    private UserService userService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private BookingEntity booking;
    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        booking = new BookingEntity();
        bookingDto = new BookingDto();
    }

    @Test
    void cancel_ShouldUpdateBookingStatus() {
        FlightEntity flight = new FlightEntity();
        flight.setId(1L);
        BookingEntity booking = new BookingEntity();
        booking.setId(1L);
        booking.setFlight(flight);
        booking.setUsers(new HashSet<>());

        given(bookingRepository.findById(anyLong())).willReturn(Optional.of(booking));

        bookingService.cancel(1L);

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());

        verify(flightService, times(1)).addSeats(eq(1L), anyInt());
    }

    @Test
    void getBookingsByUsername_ShouldReturnEmptySet_WhenUserHasNoBookings() {
        UserEntity user = new UserEntity();
        user.setBookings(new HashSet<>());

        given(userService.findByUsername(anyString())).willReturn(user);

        assertTrue(bookingService.getBookingsByUsername("testuser").isEmpty());
    }

    @Test
    void getBookingById_ShouldReturnBooking() {
        given(bookingRepository.findById(1L)).willReturn(Optional.of(new BookingEntity()));
        given(bookingMapper.toDto(any(BookingEntity.class))).willReturn(new BookingDto());

        BookingDto bookingDto = bookingService.getBookingById(1L);

        assertNotNull(bookingDto);

        then(bookingRepository).should(times(1)).findById(1L);
    }

    @Test
    void getBookingById_ShouldThrowNotFound_WhenBookingDoesNotExist() {
        given(bookingRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(BaseException.class, () -> bookingService.getBookingById(1L));

        then(bookingRepository).should(times(1)).findById(1L);
    }

    @Test
    void getBookingById_ShouldReturnBookingDto() {
        given(bookingRepository.findById(anyLong())).willReturn(Optional.of(booking));
        given(bookingMapper.toDto(any())).willReturn(bookingDto);

        BookingDto result = bookingService.getBookingById(1L);
        assertNotNull(result);
    }

    @Test
    void cancel_ShouldUpdateBookingStatus_ReturnSuccess() {
        BookingEntity booking = new BookingEntity();
        FlightEntity flight = new FlightEntity();
        flight.setId(1L);
        booking.setFlight(flight);

        given(bookingRepository.findById(anyLong())).willReturn(Optional.of(booking));

        bookingService.cancel(1L);

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
    }

    @Test
    void cancel_ShouldThrowNotFoundException_WhenBookingNotExists() {
        given(bookingRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(BaseException.class, () -> bookingService.cancel(1L));
    }

    @Test
    void updateStatus_ShouldThrowAccessDeniedException_WhenUserNotAdmin() {
        given(userService.isAdmin(anyLong())).willReturn(false);

        assertThrows(BaseException.class, () -> bookingService.updateStatus(1L, 1L, BookingStatus.CANCELLED));
    }
}
