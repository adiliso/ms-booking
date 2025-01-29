package az.edu.turing.booking.service;

import az.edu.turing.booking.model.dto.UserDto;
import az.edu.turing.booking.model.dto.request.UserCreateRequest;
import az.edu.turing.booking.model.dto.request.UserRoleUpdateRequest;
import az.edu.turing.booking.model.dto.request.UsernameUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface UserService {

    @Transactional
    UserDto create(UserCreateRequest request);

    @Transactional
    UserDto updateUsername(Long id, UsernameUpdateRequest request);

    @Transactional
    UserDto updateRole(Long id, UserRoleUpdateRequest request);

    @Transactional
    UserDto findById(Long id);

    @Transactional
    UserDto findByUsername(String username);

    @Transactional
    Page<UserDto> findAll(final int pageNumber, final int pageSize);

    boolean isAdmin(Long id);

    @Transactional
    void delete(Long id);
}
