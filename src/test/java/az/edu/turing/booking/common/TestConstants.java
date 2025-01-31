package az.edu.turing.booking.common;

import az.edu.turing.booking.domain.entity.FlightDetailsEntity;
import az.edu.turing.booking.domain.entity.FlightEntity;
import az.edu.turing.booking.model.dto.request.FlightCreateRequest;
import az.edu.turing.booking.model.dto.request.FlightUpdateRequest;
import az.edu.turing.booking.model.dto.response.FlightDetailsResponse;
import az.edu.turing.booking.model.dto.response.FlightResponse;
import az.edu.turing.booking.model.enums.AircraftType;
import az.edu.turing.booking.model.enums.Airline;
import az.edu.turing.booking.model.enums.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public class TestConstants {

    public static final Long USER_ID = 1L;
    public static final Long FLIGHT_ID = 1L;
    public static final Double PRICE = 100.10;
    public static final Integer FREE_SEATS = 10;
    public static final Integer TOTAL_SEATS = 10;
    public static final City ORIGIN_POINT = City.BAKU;
    public static final Airline AIRLINE = Airline.AZAL;
    public static final City DESTINATION_POINT = City.NEW_YORK;
    public static final AircraftType AIRCRAFT = AircraftType.BOEING;
    public static final LocalDateTime ARRIVAL_TIME = LocalDateTime.now().plusDays(2);
    public static final LocalDateTime DEPARTURE_TIME = LocalDateTime.now().plusDays(1);
//    public static Duration DURATION = Duration.between(ARRIVAL_TIME, DEPARTURE_TIME);

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

    public static Page<FlightResponse> getFlightResponseWithPage(Pageable pageable) {
        List<FlightResponse> flights = List.of(
                FlightResponse.builder()
                        .flightId(FLIGHT_ID)
                        .originPoint(ORIGIN_POINT)
                        .destinationPoint(DESTINATION_POINT)
                        .departureTime(DEPARTURE_TIME)
                        .arrivalTime(ARRIVAL_TIME)
                        .freeSeats(FREE_SEATS)
                        .price(PRICE)
                        .build()
        );
        return new PageImpl<>(flights, pageable, flights.size());
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

        FlightDetailsEntity details = FlightDetailsEntity.builder()
                .flight(flight)
                .airline(AIRLINE)
                .aircraft(AIRCRAFT)
                .freeSeats(FREE_SEATS)
                .totalSeats(TOTAL_SEATS)
                .build();

        flight.setFlightDetails(details);
        return flight;
    }

    public static FlightDetailsEntity getFlightDetailsEntity() {
        return FlightDetailsEntity.builder()
                .flight(getFlightEntity())
                .airline(AIRLINE)
                .aircraft(AIRCRAFT)
                .freeSeats(FREE_SEATS)
                .build();
    }
}
