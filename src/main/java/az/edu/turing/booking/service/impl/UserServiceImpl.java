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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto create(UserCreateRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AlreadyExistsException("User already exists with username: " + request.getUsername());
        }
        UserEntity userEntity = userMapper.toEntity(request);

        return userMapper.toDto(userRepository.save(userEntity));
    }

    @Override
    public UserDto updateUsername(Long id, UsernameUpdateRequest request) {

        UserEntity userEntity = existsUserEntity(id);
        userEntity.setUsername(request.getUsername());
        userRepository.save(userEntity);
        return userMapper.toDto(userEntity);
    }

    @Override
    public UserDto updateRole(Long id, UserRoleUpdateRequest request) {

        UserEntity userEntity = existsUserEntity(id);
        userEntity.setRole(request.getRole());
        userRepository.save(userEntity);
        return userMapper.toDto(userEntity);
    }

    @Override
    public boolean isAdmin(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found with id: " + id);
        }
        return userRepository.existsByIdAndRole(id, UserRole.ADMIN);
    }

    private UserEntity existsUserEntity(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }
}
