package az.edu.turing.booking.service;

import az.edu.turing.booking.domain.entity.UserEntity;
import az.edu.turing.booking.model.dto.UserDto;
import az.edu.turing.booking.model.dto.request.UserCreateRequest;
import az.edu.turing.booking.model.dto.request.UserRoleUpdateRequest;
import az.edu.turing.booking.model.dto.request.UsernameUpdateRequest;

public interface UserService {

    UserDto create(UserCreateRequest request);

    UserDto updateUsername(Long id, UsernameUpdateRequest request);

    UserDto updateRole(Long id, UserRoleUpdateRequest request);

    boolean isAdmin(Long id);

    UserEntity findByUsername(String username);

    boolean existsById(Long id);
}
