package az.edu.turing.booking.model.dto;

import az.edu.turing.booking.model.enums.City;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightFilter {

    @Parameter(description = "Filter by origin cities")
    @Schema(description = "List of origin cities", implementation = City.class, type = "array")
    private List<City> originPoints;

    @Parameter(description = "Filter by destination cities")
    @Schema(description = "List of destination cities", implementation = City.class, type = "array")
    private List<City> destinationPoints;

    @Parameter(description = "Start departure date (YYYY-MM-DD)", schema = @Schema(type = "string", format = "date"))
    private LocalDate startDepartureDate;

    @Parameter(description = "End departure date (YYYY-MM-DD)", schema = @Schema(type = "string", format = "date"))
    private LocalDate endDepartureDate;

    @Parameter(description = "Minimum price", schema = @Schema(type = "number", format = "double"))
    private Double minPrice;

    @Parameter(description = "Maximum price", schema = @Schema(type = "number", format = "double"))
    private Double maxPrice;
}