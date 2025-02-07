package az.edu.turing.booking.domain.repository;

import az.edu.turing.booking.domain.entity.FlightDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightDetailsRepository extends JpaRepository<FlightDetailEntity, Long> {
}
