package az.edu.turing.booking.mapper;

import az.edu.turing.booking.domain.entity.UserEntity;
import az.edu.turing.booking.model.dto.UserDto;
import az.edu.turing.booking.model.dto.request.UserCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserEntity toEntity(UserCreateRequest userCreateRequest);

    UserDto toDto(UserEntity userEntity);
}
