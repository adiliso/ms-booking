package az.edu.turing.booking.mapper;

import az.edu.turing.booking.domain.entity.BookingEntity;
import az.edu.turing.booking.model.dto.BookingDto;
import az.edu.turing.booking.model.dto.requests.BookingCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.repository.query.Param;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingDto toDto(BookingEntity entity);

    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "status", constant = "CONFIRMED")
    BookingEntity toEntity(@Param("createdBy") long createdBy, BookingCreateRequest request);
}
