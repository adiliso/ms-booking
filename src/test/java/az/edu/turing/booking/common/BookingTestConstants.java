package az.edu.turing.booking.common;

import az.edu.turing.booking.domain.entity.BookingEntity;
import az.edu.turing.booking.domain.entity.UserEntity;
import az.edu.turing.booking.model.dto.BookingDto;
import az.edu.turing.booking.model.dto.request.BookingCreateRequest;
import az.edu.turing.booking.model.dto.request.BookingUpdateRequest;
import az.edu.turing.booking.model.enums.BookingStatus;

import java.util.HashSet;
import java.util.Set;

import static az.edu.turing.booking.common.FlightTestConstant.getFlightEntity;
import static az.edu.turing.booking.model.enums.UserRole.ADMIN;
import static az.edu.turing.booking.model.enums.UserStatus.ACTIVE;

public final class BookingTestConstants {

    public static final Long BOOKING_ID = 3L;
    public static final Long USER_ID = 1L;
    public static final Long FLIGHT_ID = 2L;
    public static final int NUMBER_OF_PASSENGERS = 1;
    public static final Double TOTAL_PRICE = 400.0;
    public static final BookingStatus STATUS = BookingStatus.CONFIRMED;
    public static final String USERNAME = "test@gmail.com";
    public static final Set<String> USERNAMES = Set.of(USERNAME);

    public static BookingEntity getBookingEntity() {
        return BookingEntity.builder()
                .id(BOOKING_ID)
                .createdBy(USER_ID)
                .flight(getFlightEntity())
                .users(Set.of(getUserEntity()))
                .totalPrice(TOTAL_PRICE)
                .status(STATUS)
                .build();
    }

    public static UserEntity getUserEntity() {
        return UserEntity.builder()
                .id(USER_ID)
                .createdBy(USER_ID)
                .username(USERNAME)
                .bookings(new HashSet<>())
                .role(ADMIN)
                .status(ACTIVE)
                .build();
    }

    public static BookingCreateRequest getBookingCreateRequest() {
        return BookingCreateRequest.builder()
                .flightId(FLIGHT_ID)
                .numberOfPassengers(NUMBER_OF_PASSENGERS)
                .usernameOfPassengers(USERNAMES)
                .build();
    }

    public static BookingDto getBookingDto() {
        return BookingDto.builder()
                .id(BOOKING_ID)
                .flightId(FLIGHT_ID)
                .totalPrice(TOTAL_PRICE)
                .status(STATUS)
                .createdBy(USER_ID)
                .build();
    }

    public static BookingUpdateRequest getBookingUpdateRequest() {
        return BookingUpdateRequest.builder()
                .totalPrice(TOTAL_PRICE)
                .status(STATUS)
                .build();
    }
}
