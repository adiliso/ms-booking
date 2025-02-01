package az.edu.turing.booking.mapper;

import az.edu.turing.booking.domain.entity.FlightDetailsEntity;
import az.edu.turing.booking.domain.entity.FlightEntity;
import az.edu.turing.booking.model.dto.request.FlightCreateRequest;
import az.edu.turing.booking.model.dto.request.FlightUpdateRequest;
import az.edu.turing.booking.model.dto.response.FlightDetailsResponse;
import az.edu.turing.booking.model.dto.response.FlightResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface FlightMapper {

    FlightMapper INSTANCE = Mappers.getMapper(FlightMapper.class);

    @Mapping(target = "flightId", source = "id")
    @Mapping(target = "aircraft", source = "flightDetails.aircraft")
    @Mapping(target = "airline", source = "flightDetails.airline")
    @Mapping(target = "freeSeats", source = "flightDetails.freeSeats")
    FlightDetailsResponse toDetailedResponse(FlightEntity flightEntity);

    @Mapping(target = "flightId", source = "id")
    @Mapping(target = "freeSeats", source = "flightDetails.freeSeats")
    FlightResponse toResponse(FlightEntity flightEntity);

    FlightEntity toEntity(Long createdBy, FlightCreateRequest request);

    FlightDetailsEntity toDetailsEntity(FlightCreateRequest request);

    void updateEntity(@MappingTarget FlightEntity entity, Long updatedBy, FlightUpdateRequest request);

    void updateDetailsEntity(@MappingTarget FlightDetailsEntity entity, FlightUpdateRequest request);
}
