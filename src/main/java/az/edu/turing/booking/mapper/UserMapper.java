package az.edu.turing.booking.mapper;

import az.edu.turing.booking.domain.entity.UserEntity;
import az.edu.turing.booking.model.dto.UserDto;
import az.edu.turing.booking.model.dto.request.AdminCreateRequest;
import az.edu.turing.booking.model.dto.request.UserCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "status", constant = "ACTIVE")
    UserEntity toEntity(UserCreateRequest request);

    @Mapping(target = "role", constant = "ADMIN")
    @Mapping(target = "status", constant = "ACTIVE")
    UserEntity toEntity(AdminCreateRequest request);

    UserDto toDto(UserEntity userEntity);
}
