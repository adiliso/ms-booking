package az.edu.turing.booking.service.impl;

import az.edu.turing.booking.domain.entity.FlightDetailEntity;
import az.edu.turing.booking.domain.entity.FlightEntity;
import az.edu.turing.booking.domain.repository.FlightDetailsRepository;
import az.edu.turing.booking.domain.repository.FlightRepository;
import az.edu.turing.booking.exception.BaseException;
import az.edu.turing.booking.mapper.FlightMapper;
import az.edu.turing.booking.model.dto.FlightFilter;
import az.edu.turing.booking.model.dto.request.FlightCreateRequest;
import az.edu.turing.booking.model.dto.request.FlightStatusUpdateRequest;
import az.edu.turing.booking.model.dto.request.FlightUpdateRequest;
import az.edu.turing.booking.model.dto.response.FlightDetailsResponse;
import az.edu.turing.booking.model.dto.response.FlightResponse;
import az.edu.turing.booking.model.enums.FlightStatus;
import az.edu.turing.booking.service.FlightService;
import az.edu.turing.booking.service.UserService;
import az.edu.turing.booking.util.FlightSpecificationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static az.edu.turing.booking.model.enums.ErrorEnum.ACCESS_DENIED;
import static az.edu.turing.booking.model.enums.ErrorEnum.FLIGHT_DETAILS_NOT_FOUND;
import static az.edu.turing.booking.model.enums.ErrorEnum.FLIGHT_NOT_FOUND;
import static az.edu.turing.booking.model.enums.ErrorEnum.INVALID_OPERATION;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FlightServiceImpl implements FlightService {

    private final FlightDetailsRepository flightDetailsRepository;
    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;
    private final UserService userService;

    @Transactional
    @Override
    public Integer addSeats(Long flightId, Integer seats) {
        FlightDetailEntity entity = getFlightDetails(flightId);
        int finalFreeSeats = entity.getFreeSeats() + seats;
        if (entity.getTotalSeats() < finalFreeSeats) {
            throw new BaseException(INVALID_OPERATION, "Cannot add seats!");
        }
        entity.setFreeSeats(finalFreeSeats);

        return finalFreeSeats;
    }

    @Transactional
    @Override
    public Integer releaseSeats(Long flightId, Integer seats) {
        FlightDetailEntity entity = getFlightDetails(flightId);
        int finalFreeSeats = entity.getFreeSeats() - seats;
        if (finalFreeSeats < 0) {
            throw new BaseException(INVALID_OPERATION, "Cannot release seats!");
        }
        entity.setFreeSeats(finalFreeSeats);

        return finalFreeSeats;
    }

    @Transactional
    @Override
    public FlightResponse create(Long userId, FlightCreateRequest flightCreateRequest) {
        if (!userService.isAdmin(userId)) {
            throw new BaseException(ACCESS_DENIED);
        }

        FlightEntity flight = flightMapper.toEntity(userId, flightCreateRequest);
        FlightDetailEntity detailsEntity = flightMapper.toDetailsEntity(flightCreateRequest);

        flight.setFlightDetail(detailsEntity);

        FlightEntity savedFlight = flightRepository.save(flight);

        log.info("Creating flight...");
        return flightMapper.toResponse(savedFlight);
    }

    @Transactional
    @Override
    public FlightResponse update(Long userId, Long flightId, FlightUpdateRequest request) {
        if (!userService.isAdmin(userId)) {
            throw new BaseException(ACCESS_DENIED);
        }

        FlightEntity flight = findById(flightId);

        flightMapper.updateEntity(flight, userId, request);
        flightMapper.updateDetailsEntity(getFlightDetails(flightId), request);

        FlightEntity updatedFlight = flightRepository.save(flight);

        log.info("Flight with id {} updated successfully.", updatedFlight.getId());
        return flightMapper.toResponse(updatedFlight);
    }

    @Transactional
    @Override
    public FlightResponse updateStatus(Long userId, Long flightId, FlightStatusUpdateRequest request) {
        if (!userService.isAdmin(userId)) {
            throw new BaseException(ACCESS_DENIED);
        }

        FlightEntity flight = findById(flightId);
        flight.setStatus(request.getStatus());

        log.info("Flight status with id {} updated successfully.", flight.getId());
        return flightMapper.toResponse(flight);
    }

    @Override
    public Page<FlightResponse> getAllInNext24Hours(final int pageNumber, final int pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber, pageSize);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next24Hours = now.plusHours(24);

        Page<FlightEntity> flights = flightRepository.findByDepartureTimeBetweenAndStatusIs(now, next24Hours, pageable,
                FlightStatus.SCHEDULED);

        log.info("Finding flights in next 24 hours...");
        return flights.map(flightMapper::toResponse);
    }

    @Override
    public FlightDetailsResponse getInfoById(Long id) {
        return flightMapper.toDetailedResponse(flightRepository.findById(id)
                .orElseThrow(() -> new BaseException(FLIGHT_NOT_FOUND)));
    }

    @Override
    public Page<FlightResponse> search(FlightFilter filter, final int pageNumber, final int pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return flightRepository.findAll(FlightSpecificationUtils.getSpecification(filter), pageable)
                .map(flightMapper::toResponse);
    }

    private FlightDetailEntity getFlightDetails(Long flightId) {
        return flightDetailsRepository.findById(flightId)
                .orElseThrow(() ->
                        new BaseException(FLIGHT_DETAILS_NOT_FOUND));
    }

    private FlightEntity findById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new BaseException(FLIGHT_NOT_FOUND));
    }
}
