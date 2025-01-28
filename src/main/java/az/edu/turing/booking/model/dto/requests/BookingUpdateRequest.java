package az.edu.turing.booking.model.dto.requests;

import az.edu.turing.booking.model.enums.BookingStatus;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingUpdateRequest {

    @Min(0)
    private double totalPrice;
    private BookingStatus status;
}
