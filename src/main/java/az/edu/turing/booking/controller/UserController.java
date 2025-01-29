package az.edu.turing.booking.controller;

import az.edu.turing.booking.model.dto.UserDto;
import az.edu.turing.booking.model.dto.request.UserCreateRequest;
import az.edu.turing.booking.model.dto.request.UserRoleUpdateRequest;
import az.edu.turing.booking.model.dto.request.UsernameUpdateRequest;
import az.edu.turing.booking.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUsername(@PathVariable Long id, @Valid @RequestBody UsernameUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUsername(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUserRole(@PathVariable Long id, @Valid @RequestBody UserRoleUpdateRequest request) {
        return ResponseEntity.ok(userService.updateRole(id, request));
    }
}

