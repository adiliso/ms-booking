package az.edu.turing.booking.service;

import az.edu.turing.booking.model.dto.FlightFilter;
import az.edu.turing.booking.model.dto.request.FlightCreateRequest;
import az.edu.turing.booking.model.dto.request.FlightUpdateRequest;
import az.edu.turing.booking.model.dto.response.DetailedFlightResponse;
import az.edu.turing.booking.model.dto.response.FlightResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface FlightService {

    @Transactional
    Integer addSeats(Long flightId, Integer numberOfSeats);

    @Transactional
    Integer releaseSeats(Long flightId, Integer seats);

    @Transactional
    FlightResponse create(Long userId, Long flightId, FlightCreateRequest flightCreateRequest);

    @Transactional
    FlightResponse update(Long userId, Long flightId, FlightUpdateRequest flightUpdateRequest);

    Page<FlightResponse> getAllInNext24Hours(Pageable pageable);

    FlightResponse getInfoById(Long flightId);

    DetailedFlightResponse getDetailedInfoById(Long flightId);

    Page<FlightResponse> search(FlightFilter filter, Pageable pageable);
}
