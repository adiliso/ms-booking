package az.edu.turing.booking.common;

import az.edu.turing.booking.domain.entity.UserEntity;
import az.edu.turing.booking.model.dto.request.AdminCreateRequest;
import az.edu.turing.booking.model.dto.request.UserCreateRequest;
import az.edu.turing.booking.model.dto.request.UserStatusUpdateRequest;
import az.edu.turing.booking.model.dto.request.UsernameUpdateRequest;
import az.edu.turing.booking.model.dto.response.PageResponse;
import az.edu.turing.booking.model.dto.response.UserResponse;
import az.edu.turing.booking.model.enums.UserRole;
import az.edu.turing.booking.model.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class UserTestConstant {

    private static final Integer PAGE_SIZE = 10;
    private static final Integer PAGE_NUMBER = 0;
    public static final Long TOTAL_ELEMENTS = 1L;
    public static final Integer TOTAL_PAGES_COUNT = 1;
    public static final Long USER_ID = 1L;
    public static final String USERNAME = "joshgun@example.com";
    public static final UserStatus USER_STATUS = UserStatus.ACTIVE;
    public static final UserRole USER_ROLE = UserRole.ADMIN;

    public static UserResponse getUserResponse() {
        return UserResponse.builder()
                .id(USER_ID)
                .username(USERNAME)
                .build();
    }

    public static PageResponse<UserResponse> getUserResponsePage() {
        return PageResponse.of(List.of(getUserResponse()),
                PAGE_NUMBER,
                PAGE_SIZE,
                TOTAL_ELEMENTS,
                TOTAL_PAGES_COUNT);
    }

    public static UsernameUpdateRequest getUsernameUpdateRequest() {
        return UsernameUpdateRequest.builder()
                .username(USERNAME).build();
    }

    public static AdminCreateRequest getAdminCreateRequest() {
        return AdminCreateRequest.builder()
                .username(USERNAME).build();
    }

    public static UserCreateRequest getUserCreateRequest() {
        return UserCreateRequest.builder()
                .username(USERNAME).build();
    }

    public static UserStatusUpdateRequest getUserStatusUpdateRequest() {
        return UserStatusUpdateRequest.builder()
                .status(USER_STATUS).build();
    }

    public static Page<UserEntity> getUserEntityPage(Pageable pageable) {
        List<UserEntity> users = List.of(
                UserEntity.builder()
                        .username(USERNAME)
                        .id(USER_ID)
                        .status(USER_STATUS)
                        .role(USER_ROLE)
                        .build()
        );
        return new PageImpl<>(users, pageable, TOTAL_PAGES_COUNT);
    }

    public static UserEntity getUserEntity() {
        return UserEntity.builder()
                .username(USERNAME)
                .id(USER_ID)
                .status(USER_STATUS)
                .role(USER_ROLE)
                .build();

    }
}
