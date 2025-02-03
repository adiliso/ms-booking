package az.edu.turing.booking.model.dto.response;

import az.edu.turing.booking.model.enums.City;
import az.edu.turing.booking.model.enums.FlightStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightResponse {

    private Long flightId;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private City originPoint;
    private City destinationPoint;
    private Integer freeSeats;
    private Double price;
    private FlightStatus status;
}
