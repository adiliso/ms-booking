package az.edu.turing.booking.service.impl;

import az.edu.turing.booking.domain.entity.FlightDetailsEntity;
import az.edu.turing.booking.domain.entity.FlightEntity;
import az.edu.turing.booking.domain.repository.FlightDetailsRepository;
import az.edu.turing.booking.domain.repository.FlightRepository;
import az.edu.turing.booking.domain.repository.FlightSpecification;
import az.edu.turing.booking.domain.repository.UserRepository;
import az.edu.turing.booking.exception.BadRequestException;
import az.edu.turing.booking.exception.NotFoundException;
import az.edu.turing.booking.mapper.FlightMapper;
import az.edu.turing.booking.model.dto.request.FlightCreateRequest;
import az.edu.turing.booking.model.dto.request.FlightSearchRequest;
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
    public FlightResponse create(Long createdBy, FlightCreateRequest flightCreateRequest) {
        if (userService.isAdmin(createdBy)) {
            throw new BadRequestException("You cannot create a flight without an admin role");
        }

        FlightEntity flight = flightMapper.toEntity(createdBy, flightCreateRequest);
        FlightDetailsEntity detailsEntity = flightMapper.toDetailsEntity(flightCreateRequest);

        flight.setFlightDetails(detailsEntity);

        FlightEntity savedFlight = flightRepository.save(flight);

        log.info("Creating flight...");
        return flightMapper.toResponse(savedFlight);
    }

    @Override
    public FlightResponse update(Long updatedBy, Long flightId, FlightUpdateRequest flightUpdateRequest) {

        if (!userService.isAdmin(updatedBy)) {
            throw new BadRequestException("You cannot update a flight without an admin role");
        }

        FlightDetailsEntity detailsEntity = getFlightDetails(flightId);

        FlightEntity flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new NotFoundException("Flight not found with id: " + flightId));

        flight.setUpdatedBy(updatedBy);
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
    public Page<FlightResponse> findAllInNext24Hours(Pageable pageable) {
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
    public Page<FlightResponse> search(FlightSearchRequest flightSearchRequest, Pageable pageable) {
        Specification<FlightEntity> spec =
                Specification.where(FlightSpecification
                        .hasDepartureTimeBetween(flightSearchRequest.getDepartureDate().atStartOfDay()
                                , flightSearchRequest.getArrivalDate().atStartOfDay())
                        .and(FlightSpecification.hasPriceBetween(flightSearchRequest.getMinPrice()
                                        , flightSearchRequest.getMaxPrice())
                                .and(FlightSpecification
                                        .hasOriginPoint(flightSearchRequest.getOriginPoint()))));

        log.info("Searching for flights...");
        return flightRepository.findAll(spec, pageable).map(flightMapper::toResponse);
    }

    private FlightDetailsEntity getFlightDetails(Long flightId) {
        return flightDetailsRepository.findById(flightId)
                .orElseThrow(() ->
                        new NotFoundException("Flight details not found with id: " + flightId));
    }
}
