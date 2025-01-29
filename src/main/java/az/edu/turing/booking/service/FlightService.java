package az.edu.turing.booking.service;

import az.edu.turing.booking.domain.entity.FlightEntity;
import az.edu.turing.booking.model.dto.FlightFilter;
import az.edu.turing.booking.model.dto.request.FlightCreateRequest;
import az.edu.turing.booking.model.dto.request.FlightUpdateRequest;
import az.edu.turing.booking.model.dto.response.FlightDetailsResponse;
import az.edu.turing.booking.model.dto.response.FlightResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface FlightService {

    Integer addSeats(Long flightId, Integer numberOfSeats);

    Integer releaseSeats(Long flightId, Integer seats);

    FlightResponse create(Long userId, FlightCreateRequest flightCreateRequest);

    FlightResponse update(Long userId, Long id, FlightUpdateRequest flightUpdateRequest);

    Page<FlightResponse> getAllInNext24Hours(Pageable pageable);

    FlightResponse getInfoById(Long flightId);

    FlightEntity findById(Long id);

    FlightDetailsResponse getDetailedInfoById(Long id);

    Page<FlightResponse> search(FlightFilter filter, Pageable pageable);
}
