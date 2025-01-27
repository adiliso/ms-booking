package az.edu.turing.booking.model.dto.request;

import az.edu.turing.booking.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    private String username;
    private UserRole role;
}
