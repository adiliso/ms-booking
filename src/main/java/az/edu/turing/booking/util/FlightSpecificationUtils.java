package az.edu.turing.booking.util;

import az.edu.turing.booking.domain.entity.FlightEntity;
import az.edu.turing.booking.domain.repository.FlightSpecification;
import az.edu.turing.booking.model.dto.FlightFilter;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class FlightSpecificationUtils {

    public static Specification<FlightEntity> getSpecification(FlightFilter filter) {
        Specification<FlightEntity> spec = Specification.where(null);
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
