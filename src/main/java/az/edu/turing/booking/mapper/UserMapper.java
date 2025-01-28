package az.edu.turing.booking.mapper;

import az.edu.turing.booking.domain.entity.UserEntity;
import az.edu.turing.booking.model.dto.UserDto;
import az.edu.turing.booking.model.dto.request.UserCreateRequest;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    UserEntity toEntity(UserCreateRequest userCreateRequest);

    UserDto toDto(UserEntity userEntity);
}
