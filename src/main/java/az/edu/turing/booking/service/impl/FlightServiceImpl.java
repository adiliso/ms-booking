package az.edu.turing.booking.service.impl;

import az.edu.turing.booking.domain.entity.FlightDetailsEntity;
import az.edu.turing.booking.domain.entity.FlightEntity;
import az.edu.turing.booking.domain.repository.FlightDetailsRepository;
import az.edu.turing.booking.domain.repository.FlightRepository;
import az.edu.turing.booking.domain.repository.FlightSpecification;
import az.edu.turing.booking.exception.BadRequestException;
import az.edu.turing.booking.exception.NotFoundException;
import az.edu.turing.booking.mapper.FlightMapper;
import az.edu.turing.booking.model.dto.FlightFilter;
import az.edu.turing.booking.model.dto.request.FlightCreateRequest;
import az.edu.turing.booking.model.dto.request.FlightUpdateRequest;
import az.edu.turing.booking.model.dto.response.DetailedFlightResponse;
import az.edu.turing.booking.model.dto.response.FlightResponse;
import az.edu.turing.booking.service.FlightService;
import az.edu.turing.booking.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightDetailsRepository flightDetailsRepository;
    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;
    private final UserService userService;

    @Override
    public Integer addSeats(Long flightId, Integer seats) {
        FlightDetailsEntity entity = getFlightDetails(flightId);
        int finalFreeSeats = entity.getFreeSeats() + seats;
        if (entity.getTotalSeats() < finalFreeSeats) {
            throw new BadRequestException("Cannot add seats!");
        }
        return finalFreeSeats;
    }

    @Override
    public Integer releaseSeats(Long flightId, Integer seats) {
        FlightDetailsEntity entity = getFlightDetails(flightId);
        int finalFreeSeats = entity.getFreeSeats() - seats;
        if (finalFreeSeats < 0) {
            throw new BadRequestException("Cannot release seats!");
        }
        return finalFreeSeats;
    }

    @Override
    public FlightResponse create(Long userId, Long flightId, FlightCreateRequest flightCreateRequest) {
        if (userService.isAdmin(userId)) {
            throw new BadRequestException("You cannot create a flight without an admin role");
        }

        if (flightRepository.existsById(flightId)) {
            throw new BadRequestException("Flight already exists!");
        }

        FlightEntity flight = flightMapper.toEntity(userId, flightCreateRequest);
        FlightDetailsEntity detailsEntity = flightMapper.toDetailsEntity(flightCreateRequest);

        flight.setFlightDetails(detailsEntity);

        FlightEntity savedFlight = flightRepository.save(flight);

        log.info("Creating flight...");
        return flightMapper.toResponse(savedFlight);
    }

    @Override
    public FlightResponse update(Long userId, Long flightId, FlightUpdateRequest flightUpdateRequest) {

        if (!userService.isAdmin(userId)) {
            throw new BadRequestException("You cannot update a flight without an admin role");
        }

        FlightDetailsEntity detailsEntity = getFlightDetails(flightId);

        FlightEntity flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new NotFoundException("Flight not found with id: " + flightId));

        flight.setUpdatedBy(userId);
        flight.setPrice(flightUpdateRequest.getPrice());
        detailsEntity.setAirline(flightUpdateRequest.getAirline());
        flight.setDepartureTime(flightUpdateRequest.getDepartureTime());
        detailsEntity.setAircraft(flightUpdateRequest.getAircraft());
        flight.setArrivalTime(flightUpdateRequest.getArrivalTime());

        flight.setFlightDetails(detailsEntity);

        FlightEntity updatedFlight = flightRepository.save(flight);

        log.info("Flight with id {} updated successfully.", updatedFlight.getId());
        return flightMapper.toResponse(updatedFlight);
    }

    @Override
    public Page<FlightResponse> getAllInNext24Hours(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next24Hours = now.plusHours(24);
        Page<FlightEntity> flights = flightRepository.findByDepartureTimeBetween(now, next24Hours, pageable);

        log.info("Finding flights in next 24 hours...");
        return flights.map(flightMapper::toResponse);
    }

    @Override
    public FlightResponse getInfoById(Long id) {
        return flightRepository.findById(id)
                .map(flightMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Flight not found with id: " + id));
    }

    @Override
    public DetailedFlightResponse getDetailedInfoById(Long id) {
        return flightRepository.findById(id)
                .map(flightMapper::toDetailedResponse)
                .orElseThrow(() -> new NotFoundException("Flight details not found with id: " + id));
    }

    @Override
    public Page<FlightResponse> search(FlightFilter filter, Pageable pageable) {
        Specification<FlightEntity> spec = Specification.where(null);

        if (filter.getOriginPoints() != null && !filter.getOriginPoints().isEmpty()) {
            spec = spec.and(FlightSpecification.hasOriginPoints(filter.getOriginPoints()));
        }

        if (filter.getDestinationPoints() != null && !filter.getDestinationPoints().isEmpty()) {
            spec = spec.and(FlightSpecification.hasDestinationPoints(filter.getDestinationPoints()));
        }

        if (filter.getStartDepartureDate() != null || filter.getEndDepartureDate() != null) {
            LocalDateTime start = filter.getStartDepartureDate() != null ?
                    filter.getStartDepartureDate().atStartOfDay() : null;
            LocalDateTime end = filter.getEndDepartureDate() != null ?
                    filter.getEndDepartureDate().atTime(LocalTime.MAX) : null;
            spec = spec.and(FlightSpecification.hasDepartureTimeBetween(start, end));
        }

        if (filter.getMinPrice() != null || filter.getMaxPrice() != null) {
            spec = spec.and(FlightSpecification.hasPriceBetween(filter.getMinPrice(), filter.getMaxPrice()));
        }

        return flightRepository.findAll(spec, pageable).map(flightMapper::toResponse);
    }


    private FlightDetailsEntity getFlightDetails(Long flightId) {
        return flightDetailsRepository.findById(flightId)
                .orElseThrow(() ->
                        new NotFoundException("Flight details not found with id: " + flightId));
    }
}
