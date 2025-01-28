package az.edu.turing.booking.domain.repository;

import az.edu.turing.booking.domain.entity.FlightEntity;
import az.edu.turing.booking.model.enums.City;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FlightSpecification {

    public static Specification<FlightEntity> hasDepartureTimeBetween(LocalDateTime start, LocalDateTime end) {
        return (Root<FlightEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (start != null && end != null) {
                return criteriaBuilder.between(root.get("departureTime"), start, end);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<FlightEntity> hasPriceBetween(Double minPrice, Double maxPrice) {
        return (Root<FlightEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<FlightEntity> hasOriginPoint(City originPoint) {
        return (root, query, criteriaBuilder) -> {
            if (originPoint != null) {
                return criteriaBuilder.equal(root.get("originPoint"), originPoint);
            }
            return criteriaBuilder.conjunction();
        };
    }
}
