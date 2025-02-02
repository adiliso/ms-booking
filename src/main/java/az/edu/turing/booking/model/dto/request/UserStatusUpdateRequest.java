package az.edu.turing.booking.model.dto.request;

import az.edu.turing.booking.model.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatusUpdateRequest {

    @NotNull
    private UserStatus status;
}
