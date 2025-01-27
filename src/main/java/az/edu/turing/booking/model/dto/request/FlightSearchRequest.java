package az.edu.turing.booking.model.dto.request;

import az.edu.turing.booking.model.enums.City;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightSearchRequest {

    private City originPoint;
    private City destinationPoint;
    private LocalDate departureDate;
    private LocalDate arrivalDate;
    private Double minPrice;
    private Double maxPrice;
}
