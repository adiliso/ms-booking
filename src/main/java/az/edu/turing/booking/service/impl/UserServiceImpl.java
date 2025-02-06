package az.edu.turing.booking.service.impl;

import az.edu.turing.booking.domain.entity.UserEntity;
import az.edu.turing.booking.domain.repository.UserRepository;
import az.edu.turing.booking.exception.BaseException;
import az.edu.turing.booking.mapper.UserMapper;
import az.edu.turing.booking.model.dto.UserDto;
import az.edu.turing.booking.model.dto.request.AdminCreateRequest;
import az.edu.turing.booking.model.dto.request.UserCreateRequest;
import az.edu.turing.booking.model.dto.request.UserStatusUpdateRequest;
import az.edu.turing.booking.model.dto.request.UsernameUpdateRequest;
import az.edu.turing.booking.model.dto.response.PageResponse;
import az.edu.turing.booking.model.dto.response.UserResponse;
import az.edu.turing.booking.model.enums.UserRole;
import az.edu.turing.booking.model.enums.UserStatus;
import az.edu.turing.booking.service.UserService;
import az.edu.turing.booking.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static az.edu.turing.booking.model.enums.ErrorEnum.ACCESS_DENIED;
import static az.edu.turing.booking.model.enums.ErrorEnum.PASSWORDS_DONT_MATCH;
import static az.edu.turing.booking.model.enums.ErrorEnum.USERNAME_NOT_FOUND;
import static az.edu.turing.booking.model.enums.ErrorEnum.USER_ALREADY_EXISTS;
import static az.edu.turing.booking.model.enums.ErrorEnum.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserResponse create(UserCreateRequest request) {
        checkUsernameAlreadyExists(request.getUsername());
        UserEntity userEntity = userMapper.toEntity(request);

        return userMapper.toResponse(userRepository.save(userEntity));
    }

    @Transactional
    @Override
    public UserResponse create(Long id, AdminCreateRequest request) {
        isAdmin(id);
        checkPassword(id, request.getAdminPassword());
        checkUsernameAlreadyExists(request.getUsername());

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BaseException(PASSWORDS_DONT_MATCH);
        }
        UserEntity userEntity = userMapper.toEntity(request);
        userEntity.setPassword(UserUtils.hashPassword(request.getPassword()));

        return userMapper.toResponse(userRepository.save(userEntity));
    }

    @Override
    public boolean checkPassword(Long id, String password) {
        String hashedPassword = UserUtils.hashPassword(password);

        if (!userRepository.existsByIdAndPassword(id, hashedPassword)) {
            throw new BaseException(ACCESS_DENIED);
        }

        return true;
    }

    @Transactional
    @Override
    public UserResponse updateUsername(Long id, UsernameUpdateRequest request) {
        isAdmin(id);

        UserEntity userEntity = findById(id);
        userEntity.setUsername(request.getUsername());

        return userMapper.toResponse(userEntity);
    }

    @Transactional
    @Override
    public UserResponse updateStatus(Long id, UserStatusUpdateRequest request) {
        UserEntity userEntity = findById(id);
        userEntity.setStatus(request.getStatus());

        return userMapper.toResponse(userEntity);
    }

    @Override
    public UserDto getById(Long id) {
        return userMapper.toDto(findById(id));
    }

    @Override
    public UserDto getByUsername(String username) {
        return userMapper.toDto(findByUsername(username));
    }

    @Override
    public PageResponse<UserResponse> findAll(final int pageNumber, final int pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber, pageSize);
        var responses = userRepository.findAll(pageable).map(userMapper::toResponse);

        responses.getContent().forEach(userEntity ->
                System.out.println("Fetched UserEntity Username: " + userEntity.getUsername())
        );

        return PageResponse.of(responses.getContent(),
                pageNumber,
                pageSize,
                responses.getTotalElements(),
                responses.getTotalPages());
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BaseException(USERNAME_NOT_FOUND));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        var request = new UserStatusUpdateRequest();
        request.setStatus(UserStatus.DELETED);
        updateStatus(id, request);
    }

    @Override
    public boolean isAdmin(Long id) {
        UserEntity user = userRepository.findByIdAndStatusIs(id, UserStatus.ACTIVE)
                .orElseThrow(() -> new BaseException(USER_NOT_FOUND));

        if (!user.getRole().equals(UserRole.ADMIN)) {
            throw new BaseException(ACCESS_DENIED);
        }

        return true;
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    private UserEntity findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new BaseException(USER_NOT_FOUND));
    }

    private void checkUsernameAlreadyExists(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new BaseException(USER_ALREADY_EXISTS);
        }
    }
}
