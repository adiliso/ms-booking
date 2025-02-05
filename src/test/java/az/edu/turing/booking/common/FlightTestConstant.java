package az.edu.turing.booking.common;

import az.edu.turing.booking.domain.entity.FlightDetailEntity;
import az.edu.turing.booking.domain.entity.FlightEntity;
import az.edu.turing.booking.model.dto.request.FlightCreateRequest;
import az.edu.turing.booking.model.dto.request.FlightUpdateRequest;
import az.edu.turing.booking.model.dto.response.FlightDetailsResponse;
import az.edu.turing.booking.model.dto.response.FlightResponse;
import az.edu.turing.booking.model.dto.response.PageResponse;
import az.edu.turing.booking.model.enums.AircraftType;
import az.edu.turing.booking.model.enums.Airline;
import az.edu.turing.booking.model.enums.City;
import az.edu.turing.booking.model.enums.FlightStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class FlightTestConstant {

    public static final Integer PAGE_SIZE = 10;
    public static final Integer PAGE_NUMBER = 0;
    public static final Long TOTAL_ELEMENTS = 1L;
    public static final Integer TOTAL_PAGES_COUNT = 1;
    public static final Long USER_ID = 1L;
    public static final Long FLIGHT_ID = 1L;
    public static final Double PRICE = 200.0;
    public static final Integer FREE_SEATS = 180;
    public static final Integer TOTAL_SEATS = 200;
    public static final City ORIGIN_POINT = City.BAKU;
    public static final Airline AIRLINE = Airline.TURKISH_AIRLINES;
    public static final City DESTINATION_POINT = City.ISTANBUL;
    public static final AircraftType AIRCRAFT = AircraftType.BOEING_737;
    public static final LocalDateTime DEPARTURE_TIME =
            LocalDateTime.parse("3025-02-04T20:23:30.556551", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    public static final LocalDateTime ARRIVAL_TIME =
            LocalDateTime.parse("3025-02-04T22:23:30.556551", DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    public static FlightResponse getFlightResponse() {
        return FlightResponse.builder()
                .flightId(FLIGHT_ID)
                .originPoint(ORIGIN_POINT)
                .destinationPoint(DESTINATION_POINT)
                .departureTime(DEPARTURE_TIME)
                .arrivalTime(ARRIVAL_TIME)
                .freeSeats(FREE_SEATS)
                .price(PRICE)
                .build();
    }

    public static PageResponse<FlightResponse> getFlightResponseWithPage() {
        return PageResponse.of(List.of(getFlightResponse()),
                PAGE_NUMBER,
                PAGE_SIZE,
                TOTAL_ELEMENTS,
                TOTAL_PAGES_COUNT);
    }

    public static FlightDetailsResponse getFlightDetailsResponse() {
        return FlightDetailsResponse.builder()
                .flightId(FLIGHT_ID)
                .originPoint(ORIGIN_POINT)
                .destinationPoint(DESTINATION_POINT)
                .departureTime(DEPARTURE_TIME)
                .arrivalTime(ARRIVAL_TIME)
                .freeSeats(FREE_SEATS)
                .price(PRICE)
                .aircraft(AIRCRAFT)
                .airline(AIRLINE)
                .status(FlightStatus.SCHEDULED)
                .build();
    }


    public static FlightUpdateRequest getFlightUpdateRequest() {
        return FlightUpdateRequest.builder()
                .departureTime(DEPARTURE_TIME)
                .arrivalTime(ARRIVAL_TIME)
                .price(PRICE)
                .airline(AIRLINE)
                .aircraft(AIRCRAFT)
                .build();
    }

    public static FlightCreateRequest getFlightCreateRequest() {
        return FlightCreateRequest.builder()
                .originPoint(ORIGIN_POINT)
                .destinationPoint(DESTINATION_POINT)
                .departureTime(DEPARTURE_TIME)
                .arrivalTime(ARRIVAL_TIME)
                .airline(AIRLINE)
                .aircraft(AIRCRAFT)
                .totalSeats(TOTAL_SEATS)
                .price(PRICE)
                .build();
    }

    public static Page<FlightEntity> getFlightEntityWithPage(Pageable pageable) {
        List<FlightEntity> flights = List.of(
                FlightEntity.builder()
                        .id(FLIGHT_ID)
                        .originPoint(ORIGIN_POINT)
                        .destinationPoint(DESTINATION_POINT)
                        .departureTime(DEPARTURE_TIME)
                        .arrivalTime(ARRIVAL_TIME)
                        .price(PRICE)
                        .build()
        );
        return new PageImpl<>(flights, pageable, flights.size());
    }


    public static FlightEntity getFlightEntity() {
        FlightEntity flight = FlightEntity.builder()
                .id(FLIGHT_ID)
                .originPoint(ORIGIN_POINT)
                .destinationPoint(DESTINATION_POINT)
                .departureTime(DEPARTURE_TIME)
                .arrivalTime(ARRIVAL_TIME)
                .price(PRICE)
                .build();

        FlightDetailEntity details = FlightDetailEntity.builder()
                .flight(flight)
                .airline(AIRLINE)
                .aircraft(AIRCRAFT)
                .freeSeats(FREE_SEATS)
                .totalSeats(TOTAL_SEATS)
                .build();

        flight.setFlightDetail(details);
        return flight;
    }

    public static FlightDetailEntity getFlightDetailsEntity() {
        return FlightDetailEntity.builder()
                .flight(getFlightEntity())
                .airline(AIRLINE)
                .aircraft(AIRCRAFT)
                .freeSeats(FREE_SEATS)
                .build();
    }
}
