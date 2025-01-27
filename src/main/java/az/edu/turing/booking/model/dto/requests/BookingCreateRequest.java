package az.edu.turing.booking.model.dto.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreateRequest {

    private long flightId;

    @Min(value = 1, message = "Passengers list must contain at least one passenger.")
    @Max(value = 8, message = "Passengers list cannot contain more than 8 passengers.")
    private int numberOfPassengers;

    @NotEmpty(message = "Passengers list must contain at least one passenger.")
    private Set<String> passengers;
}
