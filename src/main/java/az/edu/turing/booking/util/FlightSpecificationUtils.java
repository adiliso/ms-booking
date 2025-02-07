package az.edu.turing.booking.util;

import az.edu.turing.booking.domain.entity.FlightEntity;
import az.edu.turing.booking.domain.specification.FlightSpecification;
import az.edu.turing.booking.model.dto.FlightFilter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FlightSpecificationUtils {

    public static Specification<FlightEntity> getSpecification(FlightFilter filter) {
        Specification<FlightEntity> spec = Specification.where(FlightSpecification.isScheduled());
        
        if (filter.getOriginPoints() != null && !filter.getOriginPoints().isEmpty()) {
            spec = spec.and(FlightSpecification.hasOriginPoints(filter.getOriginPoints()));
        }

        if (filter.getDestinationPoints() != null && !filter.getDestinationPoints().isEmpty()) {
            spec = spec.and(FlightSpecification.hasDestinationPoints(filter.getDestinationPoints()));
        }

        if (filter.getStartDepartureDate() != null || filter.getEndDepartureDate() != null) {
            LocalDateTime start = filter.getStartDepartureDate() != null ?
                    filter.getStartDepartureDate().atStartOfDay() : null;
            LocalDateTime end = filter.getEndDepartureDate() != null ?
                    filter.getEndDepartureDate().atTime(LocalTime.MAX) : null;
            spec = spec.and(FlightSpecification.hasDepartureTimeBetween(start, end));
        }

        if (filter.getMinPrice() != null || filter.getMaxPrice() != null) {
            spec = spec.and(FlightSpecification.hasPriceBetween(filter.getMinPrice(), filter.getMaxPrice()));
        }

        return spec;
    }
}
