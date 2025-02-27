package az.edu.turing.booking.controller;

import az.edu.turing.booking.model.dto.UserDto;
import az.edu.turing.booking.model.dto.request.AdminCreateRequest;
import az.edu.turing.booking.model.dto.request.UserCreateRequest;
import az.edu.turing.booking.model.dto.request.UserStatusUpdateRequest;
import az.edu.turing.booking.model.dto.request.UsernameUpdateRequest;
import az.edu.turing.booking.model.dto.response.PageResponse;
import az.edu.turing.booking.model.dto.response.UserResponse;
import az.edu.turing.booking.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @PostMapping("/admin")
    public ResponseEntity<UserResponse> create(@RequestHeader("User-Id") Long userId,
                                               @Valid @RequestBody AdminCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(userId, request));
    }

    @PatchMapping("/{id}/username")
    public ResponseEntity<UserResponse> updateUsername(@PathVariable Long id, @Valid @RequestBody UsernameUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUsername(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponse> updateStatus
            (@PathVariable Long id, @Valid @RequestBody UserStatusUpdateRequest request) {
        return ResponseEntity.ok(userService.updateStatus(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@NotNull @PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getByUsername(@NotBlank @PathVariable String username) {
        return ResponseEntity.ok(userService.getByUsername(username));
    }

    @GetMapping
    public ResponseEntity<PageResponse<UserResponse>> getAll(
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER, required = false) @Min(0) int pageNumber,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) @Min(1) int pageSize) {
        return ResponseEntity.ok(userService.findAll(pageNumber, pageSize));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

