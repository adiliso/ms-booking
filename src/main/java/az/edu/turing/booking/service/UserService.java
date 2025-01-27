package az.edu.turing.booking.service;

import az.edu.turing.booking.exception.NotFoundException;
import az.edu.turing.booking.model.dto.UserDto;
import az.edu.turing.booking.model.dto.request.CreateUserRequest;
import az.edu.turing.booking.model.dto.request.UpdateRoleRequest;
import az.edu.turing.booking.model.dto.request.UpdateUsernameRequest;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface UserService {

    UserDto create(CreateUserRequest request);

    UserDto updateUsername(long id, UpdateUsernameRequest request) throws NotFoundException;

    UserDto updateRole(long id, UpdateRoleRequest request) throws NotFoundException;

    boolean isAdmin(long id);
}
