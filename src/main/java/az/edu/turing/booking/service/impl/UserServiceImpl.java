package az.edu.turing.booking.service.impl;

import az.edu.turing.booking.domain.entity.UserEntity;
import az.edu.turing.booking.domain.repository.UserRepository;
import az.edu.turing.booking.exception.AlreadyExistsException;
import az.edu.turing.booking.exception.NotFoundException;
import az.edu.turing.booking.mapper.UserMapper;
import az.edu.turing.booking.model.dto.UserDto;
import az.edu.turing.booking.model.dto.request.UserCreateRequest;
import az.edu.turing.booking.model.dto.request.UserRoleUpdateRequest;
import az.edu.turing.booking.model.dto.request.UsernameUpdateRequest;
import az.edu.turing.booking.model.enums.UserRole;
import az.edu.turing.booking.service.UserService;
import jakarta.persistence.EntityNotFoundException;
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

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AlreadyExistsException("User already exists with username: " + request.getUsername());
        }
        UserEntity userEntity = userMapper.toEntity(request);

        return userMapper.toDto(userRepository.save(userEntity));
    }

    @Transactional
    @Override
    public UserDto updateUsername(Long id, UsernameUpdateRequest request) {

        UserEntity userEntity = findById(id);
        userEntity.setUsername(request.getUsername());
        userRepository.save(userEntity);
        return userMapper.toDto(userEntity);
    }

    @Transactional
    @Override
    public UserDto updateRole(Long id, UserRoleUpdateRequest request) {

        UserEntity userEntity = findById(id);
        userEntity.setRole(request.getRole());
        userRepository.save(userEntity);
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
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }


    @Override
    public boolean isAdmin(Long id) {
        if (!existsById(id)) {
            throw new NotFoundException("User not found with id: " + id);
        }
        return userRepository.existsByIdAndRole(id, UserRole.ADMIN);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    private UserEntity findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("User not found with id: " + id));
    }
}
