package az.edu.turing.booking.mapper;

import az.edu.turing.booking.domain.entity.FlightDetailsEntity;
import az.edu.turing.booking.domain.entity.FlightEntity;
import az.edu.turing.booking.model.dto.request.FlightCreateRequest;
import az.edu.turing.booking.model.dto.response.DetailedFlightResponse;
import az.edu.turing.booking.model.dto.response.FlightResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FlightMapper {

    FlightMapper INSTANCE = Mappers.getMapper(FlightMapper.class);

    @Mapping(target = "flightId", source = "id")
    @Mapping(target = "aircraft", source = "flightDetails.aircraft")
    @Mapping(target = "airline", source = "flightDetails.airline")
    DetailedFlightResponse toDetailedResponse(FlightEntity flightEntity);

    FlightResponse toResponse(FlightEntity flightEntity);

    FlightEntity toEntity(FlightCreateRequest request);

    FlightDetailsEntity toDetailsEntity(FlightCreateRequest request);
}
