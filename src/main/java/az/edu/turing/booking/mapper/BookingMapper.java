package az.edu.turing.booking.mapper;

import az.edu.turing.booking.domain.entity.BookingEntity;
import az.edu.turing.booking.model.dto.BookingDto;
import az.edu.turing.booking.model.dto.request.BookingCreateRequest;
import az.edu.turing.booking.model.dto.request.BookingUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    BookingDto toDto(BookingEntity entity);

    @Mapping(target = "status", constant = "CONFIRMED")
    BookingEntity toEntity(Long createdBy, BookingCreateRequest request);

    void updateEntity(@MappingTarget BookingEntity entity, Long updatedBy, BookingUpdateRequest request);
}
