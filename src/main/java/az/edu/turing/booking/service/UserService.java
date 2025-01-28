package az.edu.turing.booking.service;

import az.edu.turing.booking.model.dto.UserDto;
import az.edu.turing.booking.model.dto.request.UserCreateRequest;
import az.edu.turing.booking.model.dto.request.UserRoleUpdateRequest;
import az.edu.turing.booking.model.dto.request.UsernameUpdateRequest;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface UserService {

    @Transactional
    UserDto create(UserCreateRequest request);

    @Transactional
    UserDto updateUsername(Long id, UsernameUpdateRequest request);

    @Transactional
    UserDto updateRole(Long id, UserRoleUpdateRequest request);

    boolean isAdmin(Long id);
}
