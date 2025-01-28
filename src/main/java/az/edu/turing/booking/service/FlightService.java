package az.edu.turing.booking.service;

import az.edu.turing.booking.model.dto.request.FlightCreateRequest;
import az.edu.turing.booking.model.dto.request.FlightSearchRequest;
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
    FlightResponse create(Long createdBy, FlightCreateRequest flightCreateRequest);

    @Transactional
    FlightResponse update(Long updatedBy, Long id, FlightUpdateRequest flightUpdateRequest);

    Page<FlightResponse> findAllInNext24Hours(Pageable pageable);

    FlightResponse getInfoById(Long id);

    DetailedFlightResponse getDetailedInfoById(Long id);

    Page<FlightResponse> search(FlightSearchRequest flightSearchRequest, Pageable pageable);
}
