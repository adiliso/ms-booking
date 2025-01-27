package az.edu.turing.booking.model.dto.request;

import az.edu.turing.booking.model.enums.AircraftType;
import az.edu.turing.booking.model.enums.Airline;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightUpdateRequest {

    private Long userId;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Double price;
    private Airline airline;
    private AircraftType aircraft;
}
