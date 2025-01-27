package az.edu.turing.booking.service.impl;

import az.edu.turing.booking.domain.entity.UserEntity;
import az.edu.turing.booking.domain.repository.UserRepository;
import az.edu.turing.booking.exception.AlreadyExistsException;
import az.edu.turing.booking.exception.NotFoundException;
import az.edu.turing.booking.mapper.UserMapper;
import az.edu.turing.booking.model.dto.UserDto;
import az.edu.turing.booking.model.dto.request.CreateUserRequest;
import az.edu.turing.booking.model.dto.request.UpdateRoleRequest;
import az.edu.turing.booking.model.dto.request.UpdateUsernameRequest;
import az.edu.turing.booking.model.enums.UserRole;
import az.edu.turing.booking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto create(CreateUserRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AlreadyExistsException("User already exists with username: " + request.getUsername());
        }
        UserEntity userEntity = userMapper.toEntity(request);
        userRepository.save(userEntity);
        return userMapper.toDto(userEntity);
    }

    @Override
    @Transactional
    public UserDto updateUsername(long id, UpdateUsernameRequest request) {

        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        userEntity.setUsername(request.getUsername());
        userRepository.save(userEntity);
        return userMapper.toDto(userEntity);
    }

    @Override
    @Transactional
    public UserDto updateRole(long id, UpdateRoleRequest request) {

        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        userEntity.setRole(request.getRole());
        userRepository.save(userEntity);
        return userMapper.toDto(userEntity);
    }

    @Override
    public boolean isAdmin(long id) {

        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        return userEntity.getRole() == UserRole.ADMIN;
    }
}
