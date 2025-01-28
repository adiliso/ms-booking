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
    UserDto updateUsername(long id, UsernameUpdateRequest request);

    @Transactional
    UserDto updateRole(long id, UserRoleUpdateRequest request);

    boolean isAdmin(long id);
}
