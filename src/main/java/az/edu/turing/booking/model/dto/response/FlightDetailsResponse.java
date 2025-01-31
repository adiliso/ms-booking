package az.edu.turing.booking.model.dto.response;

import az.edu.turing.booking.model.enums.AircraftType;
import az.edu.turing.booking.model.enums.Airline;
import az.edu.turing.booking.model.enums.City;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightDetailsResponse {

    private Long flightId;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private City originPoint;
    private City destinationPoint;
    private Integer freeSeats;
    private Double price;
    private AircraftType aircraft;
    private Airline airline;
    private Duration duration;

    public Duration getDuration() {
        if (departureTime != null && arrivalTime != null) {
            return Duration.between(departureTime, arrivalTime);
        }
        return Duration.ZERO;
    }
}
