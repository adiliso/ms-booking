package az.edu.turing.booking.mapper;

import az.edu.turing.booking.domain.entity.FlightDetailsEntity;
import az.edu.turing.booking.domain.entity.FlightEntity;
import az.edu.turing.booking.model.dto.request.FlightCreateRequest;
import az.edu.turing.booking.model.dto.response.DetailedFlightResponse;
import az.edu.turing.booking.model.dto.response.FlightResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.repository.query.Param;

@Mapper(componentModel = "spring")
public interface FlightMapper {

    FlightMapper INSTANCE = Mappers.getMapper(FlightMapper.class);

    @Mapping(target = "flightId", source = "id")
    @Mapping(target = "aircraft", source = "flightDetails.aircraft")
    @Mapping(target = "airline", source = "flightDetails.airline")
    DetailedFlightResponse toDetailedResponse(FlightEntity flightEntity);

    FlightResponse toResponse(FlightEntity flightEntity);

    @Mapping(target = "createdBy", source = "createdBy")
    FlightEntity toEntity(@Param("createdBy") Long createdBy, FlightCreateRequest request);

    FlightDetailsEntity toDetailsEntity(FlightCreateRequest request);
}
