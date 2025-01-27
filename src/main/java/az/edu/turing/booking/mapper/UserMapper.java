package az.edu.turing.booking.mapper;

import az.edu.turing.booking.domain.entity.UserEntity;
import az.edu.turing.booking.model.dto.UserDto;
import az.edu.turing.booking.model.dto.request.CreateUserRequest;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    UserEntity toEntity(CreateUserRequest createUserRequest);

    UserDto toDto(UserEntity userEntity);
}
