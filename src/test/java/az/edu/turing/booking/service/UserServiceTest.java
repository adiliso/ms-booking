package az.edu.turing.booking.service;

import az.edu.turing.booking.domain.entity.UserEntity;
import az.edu.turing.booking.domain.repository.UserRepository;
import az.edu.turing.booking.exception.BaseException;
import az.edu.turing.booking.mapper.UserMapper;
import az.edu.turing.booking.model.dto.UserDto;
import az.edu.turing.booking.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static az.edu.turing.booking.common.UserTestConstant.USERNAME;
import static az.edu.turing.booking.common.UserTestConstant.USER_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Spy
    UserMapper userMapper = UserMapper.INSTANCE;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void findByUsername_Should_Return_UserEntity() {
        UserEntity expectedUser = UserEntity.builder()
                .id(USER_ID)
                .username(USERNAME)
                .build();

        given(userRepository.findByUsername(USERNAME)).willReturn(Optional.of(expectedUser));

        UserEntity actualUser = userService.findByUsername(USERNAME);

        assertThat(actualUser).isNotNull();
        assertThat(actualUser.getUsername()).isEqualTo(USERNAME);
        assertThat(actualUser.getId()).isEqualTo(USER_ID);

        then(userRepository).should(times(1)).findByUsername(USERNAME);
    }

    @Test
    void findByUsername_Should_Throw_Exception_WhenUserNotFound() {
        given(userRepository.findByUsername(USERNAME)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByUsername(USERNAME))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("Username not found");

        then(userRepository).should(times(1)).findByUsername(USERNAME);
    }

    @Test
    void getById_Should_Return_UserDto() {
        UserEntity mockUserEntity = new UserEntity();
        mockUserEntity.setId(USER_ID);
        mockUserEntity.setUsername(USERNAME);

        UserDto mockUserDto = new UserDto();
        mockUserDto.setId(USER_ID);
        mockUserDto.setUsername(USERNAME);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(mockUserEntity));
        when(userMapper.toDto(mockUserEntity)).thenReturn(mockUserDto);

        UserDto result = userService.getById(USER_ID);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(USER_ID);
        assertThat(result.getUsername()).isEqualTo(USERNAME);

        verify(userRepository, times(1)).findById(USER_ID);
        verify(userMapper, times(1)).toDto(mockUserEntity);
    }

    @Test
    void getById_Should_Throw_NotFoundException_WhenIdNotFound() {
        given(userRepository.findById(USER_ID)).willReturn(Optional.empty());

        BaseException ex = Assertions.assertThrows(BaseException.class,
                () -> userService.getById(USER_ID));

        Assertions.assertEquals("User not found", ex.getMessage());

        then(userRepository).should(times(1)).findById(USER_ID);
    }
}
