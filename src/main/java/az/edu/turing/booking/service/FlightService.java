package az.edu.turing.booking.service;

import az.edu.turing.booking.model.dto.FlightFilter;
import az.edu.turing.booking.model.dto.request.FlightCreateRequest;
import az.edu.turing.booking.model.dto.request.FlightUpdateRequest;
import az.edu.turing.booking.model.dto.response.FlightDetailsResponse;
import az.edu.turing.booking.model.dto.response.FlightResponse;
import org.springframework.data.domain.Page;

public interface FlightService {

    Integer addSeats(Long flightId, Integer numberOfSeats);

    Integer releaseSeats(Long flightId, Integer seats);

    FlightResponse create(Long userId, FlightCreateRequest flightCreateRequest);

    FlightResponse update(Long userId, Long id, FlightUpdateRequest flightUpdateRequest);

    Page<FlightResponse> getAllInNext24Hours(final int pageNumber, final int pageSize);

    FlightDetailsResponse getInfoById(Long id);

    Page<FlightResponse> search(FlightFilter filter, final int pageNumber, final int pageSize);
}
