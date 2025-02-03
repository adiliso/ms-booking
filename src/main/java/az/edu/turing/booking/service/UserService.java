package az.edu.turing.booking.service;

import az.edu.turing.booking.domain.entity.UserEntity;
import az.edu.turing.booking.model.dto.UserDto;
import az.edu.turing.booking.model.dto.request.AdminCreateRequest;
import az.edu.turing.booking.model.dto.request.UserCreateRequest;
import az.edu.turing.booking.model.dto.request.UserStatusUpdateRequest;
import az.edu.turing.booking.model.dto.request.UsernameUpdateRequest;
import org.springframework.data.domain.Page;

public interface UserService {

    UserDto create(UserCreateRequest request);

    UserDto create(Long userId, AdminCreateRequest request);

    UserDto updateUsername(Long id, UsernameUpdateRequest request);

    UserDto updateStatus(Long id, UserStatusUpdateRequest request);

    UserDto getById(Long id);

    UserEntity findByUsername(String username);

    UserDto getByUsername(String username);

    Page<UserDto> findAll(final int pageNumber, final int pageSize);

    boolean isAdmin(Long id);

    boolean existsById(Long id);

    void delete(Long id);

    boolean checkPassword(Long id, String password);
}
