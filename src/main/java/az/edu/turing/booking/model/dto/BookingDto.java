package az.edu.turing.booking.model.dto;

import az.edu.turing.booking.model.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {

    private Long id;
    private Long flightId;
    private Double totalPrice;
    private BookingStatus status;
}
