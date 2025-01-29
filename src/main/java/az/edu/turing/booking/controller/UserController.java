package az.edu.turing.booking.controller;

import az.edu.turing.booking.model.dto.UserDto;
import az.edu.turing.booking.model.dto.request.UserCreateRequest;
import az.edu.turing.booking.model.dto.request.UserRoleUpdateRequest;
import az.edu.turing.booking.model.dto.request.UsernameUpdateRequest;
import az.edu.turing.booking.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static az.edu.turing.booking.model.constant.PageConstants.DEFAULT_PAGE_NUMBER;
import static az.edu.turing.booking.model.constant.PageConstants.DEFAULT_PAGE_SIZE;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @PatchMapping("/{id}/username")
    public ResponseEntity<UserDto> updateUsername(@PathVariable Long id, @Valid @RequestBody UsernameUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUsername(id, request));
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<UserDto> updateUserRole(@PathVariable Long id, @Valid @RequestBody UserRoleUpdateRequest request) {
        return ResponseEntity.ok(userService.updateRole(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@NotNull @PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@NotBlank @PathVariable String username) {
        return ResponseEntity.ok(userService.getByUsername(username));
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER, required = false) @Min(0) int pageNumber,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) @Min(1) int pageSize) {
        return ResponseEntity.ok(userService.findAll(pageNumber, pageSize));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> deleteUserById(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

