package az.edu.turing.booking.model.dto.request;

import az.edu.turing.booking.domain.entity.FlightDetailsEntity;
import az.edu.turing.booking.model.enums.AircraftType;
import az.edu.turing.booking.model.enums.Airline;
import az.edu.turing.booking.model.enums.City;
import az.edu.turing.booking.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightCreateRequest {

    private City originPoint;
    private City destinationPoint;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Double price;
    private Integer totalSeats;
    private Airline airline;
    private AircraftType aircraft;
}
