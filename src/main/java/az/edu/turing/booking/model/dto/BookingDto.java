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

    private long id;
    private long flightId;
    private double totalPrice;
    private BookingStatus status;
}
