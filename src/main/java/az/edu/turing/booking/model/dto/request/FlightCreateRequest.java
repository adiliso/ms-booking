package az.edu.turing.booking.model.dto.request;

import az.edu.turing.booking.model.enums.AircraftType;
import az.edu.turing.booking.model.enums.Airline;
import az.edu.turing.booking.model.enums.City;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Origin city is required")
    private City originPoint;

    @NotNull(message = "Destination city is required")
    private City destinationPoint;

    @NotNull(message = "Departure time is required")
    @Future(message = "Departure time must be in the future")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival time is required")
    @Future(message = "Arrival time must be in the future")
    private LocalDateTime arrivalTime;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be a positive number")
    private Double price;

    @NotNull(message = "Total seats are required")
    @Min(value = 1, message = "Total seats must be at least 1")
    private Integer totalSeats;

    @NotNull(message = "Airline is required")
    private Airline airline;

    @NotNull(message = "Aircraft is required")
    private AircraftType aircraft;
}
