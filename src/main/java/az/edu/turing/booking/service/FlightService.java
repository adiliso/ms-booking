package az.edu.turing.booking.service;

import az.edu.turing.booking.domain.entity.FlightEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Transactional(readOnly = true)
public interface FlightService {

    void create(FlightEntity flightEntity);

    FlightEntity update(long id, FlightEntity flightEntity);

    Collection<FlightEntity> findAllInNext24Hours();

    FlightEntity showInfoById(long id);

    FlightEntity showDetailedInfoById(long id);

    Collection<FlightEntity> search(FlightEntity flightEntity);
}
