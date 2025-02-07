package az.edu.turing.booking.service;

import az.edu.turing.booking.domain.entity.BookingEntity;
import az.edu.turing.booking.domain.entity.UserEntity;
import az.edu.turing.booking.domain.repository.BookingRepository;
import az.edu.turing.booking.domain.repository.UserRepository;
import az.edu.turing.booking.exception.BaseException;
import az.edu.turing.booking.mapper.BookingMapper;
import az.edu.turing.booking.model.dto.BookingDto;
import az.edu.turing.booking.model.dto.response.FlightDetailsResponse;
import az.edu.turing.booking.model.dto.response.PageResponse;
import az.edu.turing.booking.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static az.edu.turing.booking.common.BookingTestConstants.BOOKING_ID;
import static az.edu.turing.booking.common.BookingTestConstants.FLIGHT_ID;
import static az.edu.turing.booking.common.BookingTestConstants.NUMBER_OF_PASSENGERS;
import static az.edu.turing.booking.common.BookingTestConstants.PAGE_NUMBER;
import static az.edu.turing.booking.common.BookingTestConstants.PAGE_REQUEST;
import static az.edu.turing.booking.common.BookingTestConstants.PAGE_SIZE;
import static az.edu.turing.booking.common.BookingTestConstants.USERNAME;
import static az.edu.turing.booking.common.BookingTestConstants.USER_ID;
import static az.edu.turing.booking.common.BookingTestConstants.getBookingCreateRequest;
import static az.edu.turing.booking.common.BookingTestConstants.getBookingDto;
import static az.edu.turing.booking.common.BookingTestConstants.getBookingDtoDtoWithPage;
import static az.edu.turing.booking.common.BookingTestConstants.getBookingEntity;
import static az.edu.turing.booking.common.BookingTestConstants.getBookingEntityWithPage;
import static az.edu.turing.booking.common.BookingTestConstants.getBookingUpdateRequest;
import static az.edu.turing.booking.common.BookingTestConstants.getUserEntity;
import static az.edu.turing.booking.common.FlightTestConstant.getFlightDetailsResponse;
import static az.edu.turing.booking.model.enums.ErrorEnum.FLIGHT_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Spy
    private BookingMapper bookingMapper = BookingMapper.INSTANCE;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FlightService flightService;

    @Mock
    private UserService userService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void create_Should_Return_Success() {
        given(userService.existsById(USER_ID)).willReturn(true);
        given(flightService.getInfoById(FLIGHT_ID)).willReturn(getFlightDetailsResponse());
        given(userService.findByUsername(USERNAME)).willReturn(getUserEntity());
        given(flightService.releaseSeats(FLIGHT_ID, NUMBER_OF_PASSENGERS)).willReturn(any());
        given(bookingRepository.save(getBookingEntity())).willReturn(getBookingEntity());

        BookingDto booking = bookingService.create(USER_ID, getBookingCreateRequest());

        assertNotNull(booking);
        assertEquals(getBookingDto(), booking);

        then(userService).should(times(1)).existsById(USER_ID);
        then(flightService).should(times(1)).getInfoById(FLIGHT_ID);
        then(userService).should(times(1)).findByUsername(USERNAME);
        then(flightService).should(times(1)).releaseSeats(FLIGHT_ID, NUMBER_OF_PASSENGERS);
        then(bookingRepository).should(times(1)).save(any(BookingEntity.class));
    }

    @Test
    void create_Should_Throw_Exception_When_UserNotFound() {
        given(userService.existsById(USER_ID)).willReturn(false);

        BaseException ex = assertThrows(BaseException.class,
                () -> bookingService.create(USER_ID, getBookingCreateRequest()));

        assertEquals(404, ex.getCode());
        assertEquals("User not found", ex.getMessage());

        then(userService).should(times(1)).existsById(USER_ID);
    }

    @Test
    void create_Should_Throw_Exception_When_FlightNotFound() {
        given(userService.existsById(USER_ID)).willReturn(true);
        given(flightService.getInfoById(FLIGHT_ID)).willThrow(new BaseException(FLIGHT_NOT_FOUND));

        BaseException ex = assertThrows(BaseException.class,
                () -> bookingService.create(USER_ID, getBookingCreateRequest()));

        assertEquals(404, ex.getCode());
        assertEquals("Flight not found", ex.getMessage());

        then(userService).should(times(1)).existsById(USER_ID);
        then(flightService).should(times(1)).getInfoById(FLIGHT_ID);
    }

    @Test
    void create_Should_Throw_Exception_When_TooLate() {
        FlightDetailsResponse flightDetail = getFlightDetailsResponse();
        flightDetail.setDepartureTime(LocalDateTime.now().plusMinutes(29));

        given(userService.existsById(USER_ID)).willReturn(true);
        given(flightService.getInfoById(FLIGHT_ID)).willReturn(flightDetail);

        BaseException ex = assertThrows(BaseException.class,
                () -> bookingService.create(USER_ID, getBookingCreateRequest()));

        assertEquals(405, ex.getCode());
        assertEquals("Too late for booking", ex.getMessage());

        then(userService).should(times(1)).existsById(USER_ID);
        then(flightService).should(times(1)).getInfoById(FLIGHT_ID);
    }

    @Test
    void create_Should_Throw_Exception_When_LackOfFreeSeats() {
        FlightDetailsResponse flightDetail = getFlightDetailsResponse();
        flightDetail.setFreeSeats(NUMBER_OF_PASSENGERS - 1);

        given(userService.existsById(USER_ID)).willReturn(true);
        given(flightService.getInfoById(FLIGHT_ID)).willReturn(flightDetail);

        BaseException ex = assertThrows(BaseException.class,
                () -> bookingService.create(USER_ID, getBookingCreateRequest()));

        String expectedMessage =
                String.format("There are only %d free seats in this flight", flightDetail.getFreeSeats());

        assertEquals(405, ex.getCode());
        assertEquals(expectedMessage, ex.getMessage());

        then(userService).should(times(1)).existsById(USER_ID);
        then(flightService).should(times(1)).getInfoById(FLIGHT_ID);
    }

    @Test
    void update_Should_Return_Success() {
        given(userService.existsById(USER_ID)).willReturn(true);
        given(bookingRepository.findById(BOOKING_ID)).willReturn(Optional.of(getBookingEntity()));

        BookingDto bookingDto = bookingService.update(USER_ID, BOOKING_ID, getBookingUpdateRequest());

        assertNotNull(bookingDto);
        assertEquals(getBookingDto(), bookingDto);

        then(userService).should(times(1)).existsById(USER_ID);
        then(bookingRepository).should(times(1)).findById(BOOKING_ID);
    }

    @Test
    void update_Should_Throw_Exception_When_UserNotFound() {
        given(userService.existsById(USER_ID)).willReturn(false);

        BaseException ex = assertThrows(BaseException.class,
                () -> bookingService.update(USER_ID, BOOKING_ID, getBookingUpdateRequest()));

        assertEquals(404, ex.getCode());
        assertEquals("User not found", ex.getMessage());

        then(userService).should(times(1)).existsById(USER_ID);
    }

    @Test
    void update_Should_Throw_Exception_When_BookingNotFound() {
        given(userService.existsById(USER_ID)).willReturn(true);
        given(bookingRepository.findById(BOOKING_ID)).willReturn(Optional.empty());

        BaseException ex = assertThrows(BaseException.class,
                () -> bookingService.update(USER_ID, BOOKING_ID, getBookingUpdateRequest()));

        assertEquals(404, ex.getCode());
        assertEquals("Booking not found", ex.getMessage());

        then(userService).should(times(1)).existsById(USER_ID);
        then(bookingRepository).should(times(1)).findById(BOOKING_ID);
    }

    @Test
    void getBookingsByUsername_Should_Return_Success() {
        UserEntity userEntity = getUserEntity();
        userEntity.setBookings(Set.of(getBookingEntity()));
        given(bookingRepository.findAllByUsersUsername(USERNAME, PAGE_REQUEST))
                .willReturn(getBookingEntityWithPage());

        PageResponse<BookingDto> bookings = bookingService.getBookingsByUsername(USERNAME, PAGE_NUMBER, PAGE_SIZE);

        assertNotNull(bookings);
        assertEquals(getBookingDtoDtoWithPage(), bookings);

        then(bookingRepository).should(times(1)).findAllByUsersUsername(USERNAME, PAGE_REQUEST);
    }

    @Test
    void getById_Should_Return_Success() {
        given(bookingRepository.findById(BOOKING_ID)).willReturn(Optional.of(getBookingEntity()));

        BookingDto bookingDto = bookingService.getBookingById(BOOKING_ID);

        assertNotNull(bookingDto);
        assertEquals(getBookingDto(), bookingDto);

        then(bookingRepository).should(times(1)).findById(BOOKING_ID);
    }

    @Test
    void getById_Should_throw_Exception_When_BookingNotFound() {
        given(bookingRepository.findById(BOOKING_ID)).willReturn(Optional.empty());

        BaseException ex = assertThrows(BaseException.class,
                () -> bookingService.getBookingById(BOOKING_ID));

        assertEquals(404, ex.getCode());
        assertEquals("Booking not found", ex.getMessage());

        then(bookingRepository).should(times(1)).findById(BOOKING_ID);
    }
}
