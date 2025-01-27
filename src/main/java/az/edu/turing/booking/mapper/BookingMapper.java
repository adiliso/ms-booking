package az.edu.turing.booking.mapper;

import az.edu.turing.booking.domain.entity.BookingEntity;
import az.edu.turing.booking.model.dto.BookingDto;
import org.mapstruct.Mapper;

@Mapper
public interface BookingMapper {

    BookingDto toDto(BookingEntity entity);
}
