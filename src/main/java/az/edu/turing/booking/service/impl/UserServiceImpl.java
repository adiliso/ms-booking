package az.edu.turing.booking.service.impl;

import az.edu.turing.booking.domain.entity.UserEntity;
import az.edu.turing.booking.domain.repository.UserRepository;
import az.edu.turing.booking.exception.AccessDeniedException;
import az.edu.turing.booking.exception.AlreadyExistsException;
import az.edu.turing.booking.exception.InvalidInputException;
import az.edu.turing.booking.exception.InvalidOperationException;
import az.edu.turing.booking.exception.NotFoundException;
import az.edu.turing.booking.mapper.UserMapper;
import az.edu.turing.booking.model.dto.UserDto;
import az.edu.turing.booking.model.dto.request.AdminCreateRequest;
import az.edu.turing.booking.model.dto.request.UserCreateRequest;
import az.edu.turing.booking.model.dto.request.UserStatusUpdateRequest;
import az.edu.turing.booking.model.dto.request.UsernameUpdateRequest;
import az.edu.turing.booking.model.enums.UserRole;
import az.edu.turing.booking.model.enums.UserStatus;
import az.edu.turing.booking.service.UserService;
import az.edu.turing.booking.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserDto create(UserCreateRequest request) {
        checkUsernameAlreadyExists(request.getUsername());
        UserEntity userEntity = userMapper.toEntity(request);

        return userMapper.toDto(userRepository.save(userEntity));
    }

    @Transactional
    @Override
    public UserDto create(Long id, AdminCreateRequest request) {
        if (!isAdmin(id)) {
            throw new AccessDeniedException("User is not an admin");
        }
        if (!checkPassword(id, request.getAdminPassword())) {
            throw new AccessDeniedException("Password is incorrect");
        }

        checkUsernameAlreadyExists(request.getUsername());
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidInputException("Passwords don't match");
        }
        UserEntity userEntity = userMapper.toEntity(request);
        userEntity.setPassword(UserUtils.hashPassword(request.getPassword()));

        return userMapper.toDto(userRepository.save(userEntity));
    }

    @Override
    public boolean checkPassword(Long id, String password) {
        String hashedPassword = UserUtils.hashPassword(password);
        return userRepository.existsByIdAndPassword(id, hashedPassword);
    }

    @Transactional
    @Override
    public UserDto updateUsername(Long id, UsernameUpdateRequest request) {
        if(isAdmin(id)) {
            throw new InvalidOperationException("Can't change username");
        }
        UserEntity userEntity = findById(id);
        userEntity.setUsername(request.getUsername());

        return userMapper.toDto(userEntity);
    }

    @Transactional
    @Override
    public UserDto updateStatus(Long id, UserStatusUpdateRequest request) {
        UserEntity userEntity = findById(id);
        userEntity.setStatus(request.getStatus());

        return userMapper.toDto(userEntity);
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
    public Page<UserDto> findAll(final int pageNumber, final int pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return userRepository.findAll(pageable).map(userMapper::toDto);
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username));
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
        UserEntity user = findById(id);
        if (!user.getStatus().equals(UserStatus.ACTIVE)) {
            throw new NotFoundException("User not found with id: " + id);
        }
        return user.getRole().equals(UserRole.ADMIN);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    private UserEntity findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("User not found with id: " + id));
    }

    private void checkUsernameAlreadyExists(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new AlreadyExistsException("User already exists with username: " + username);
        }
    }
}
