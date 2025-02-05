package az.edu.turing.booking.service;

import az.edu.turing.booking.model.dto.FlightFilter;
import az.edu.turing.booking.model.dto.request.FlightCreateRequest;
import az.edu.turing.booking.model.dto.request.FlightStatusUpdateRequest;
import az.edu.turing.booking.model.dto.request.FlightUpdateRequest;
import az.edu.turing.booking.model.dto.response.FlightDetailsResponse;
import az.edu.turing.booking.model.dto.response.FlightResponse;
import az.edu.turing.booking.model.dto.response.PageResponse;

public interface FlightService {

    Integer addSeats(Long flightId, Integer numberOfSeats);

    Integer releaseSeats(Long flightId, Integer seats);

    FlightResponse create(Long userId, FlightCreateRequest flightCreateRequest);

    FlightResponse update(Long userId, Long flightId, FlightUpdateRequest flightUpdateRequest);

    FlightResponse updateStatus(Long userId, Long flightId, FlightStatusUpdateRequest flightStatusUpdateRequest);

    PageResponse<FlightResponse> getAllInNext24Hours(final int pageNumber, final int pageSize);

    FlightDetailsResponse getInfoById(Long id);

    PageResponse<FlightResponse> search(FlightFilter filter, final int pageNumber, final int pageSize);
}
