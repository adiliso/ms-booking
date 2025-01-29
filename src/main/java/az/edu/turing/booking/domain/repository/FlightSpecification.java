package az.edu.turing.booking.domain.repository;

import az.edu.turing.booking.domain.entity.FlightEntity;
import az.edu.turing.booking.model.enums.City;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FlightSpecification {

    public static Specification<FlightEntity> hasOriginPoints(List<City> origins) {
        return (root, query, criteriaBuilder) ->
                root.get("originPoint").in(origins);
    }

    public static Specification<FlightEntity> hasDestinationPoints(List<City> destinations) {
        return (root, query, criteriaBuilder) ->
                root.get("destinationPoint").in(destinations);
    }

    public static Specification<FlightEntity> hasDepartureTimeBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, criteriaBuilder) -> {
            if (start != null && end != null) {
                return criteriaBuilder.between(root.get("departureTime"), start, end);
            } else if (start != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("departureTime"), start);
            } else if (end != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("departureTime"), end);
            }
            return null;
        };
    }

    public static Specification<FlightEntity> hasPriceBetween(Double min, Double max) {
        return (root, query, criteriaBuilder) -> {
            if (min != null && max != null) {
                return criteriaBuilder.between(root.get("price"), min, max);
            } else if (min != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), min);
            } else if (max != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), max);
            }
            return null;
        };
    }
}
