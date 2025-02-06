package az.edu.turing.booking.service;

import az.edu.turing.booking.domain.entity.UserEntity;
import az.edu.turing.booking.model.dto.UserDto;
import az.edu.turing.booking.model.dto.request.AdminCreateRequest;
import az.edu.turing.booking.model.dto.request.UserCreateRequest;
import az.edu.turing.booking.model.dto.request.UserStatusUpdateRequest;
import az.edu.turing.booking.model.dto.request.UsernameUpdateRequest;
import az.edu.turing.booking.model.dto.response.PageResponse;
import az.edu.turing.booking.model.dto.response.UserResponse;

public interface UserService {

    UserResponse create(UserCreateRequest request);

    UserResponse create(Long userId, AdminCreateRequest request);

    UserResponse updateUsername(Long id, UsernameUpdateRequest request);

    UserResponse updateStatus(Long id, UserStatusUpdateRequest request);

    UserResponse getById(Long id);

    UserEntity findByUsername(String username);

    UserResponse getByUsername(String username);

    PageResponse<UserResponse> findAll(final int pageNumber, final int pageSize);

    boolean isAdmin(Long id);

    boolean existsById(Long id);

    void delete(Long id);

    boolean checkPassword(Long id, String password);
}
