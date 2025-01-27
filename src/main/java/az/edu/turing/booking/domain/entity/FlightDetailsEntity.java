package az.edu.turing.booking.domain.entity;

import az.edu.turing.booking.model.enums.AircraftType;
import az.edu.turing.booking.model.enums.Airline;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "flight_details")
@EqualsAndHashCode(callSuper = true)
public class FlightDetailsEntity extends BaseEntity {

    @Id
    @Column(name = "flight_id")
    private Long id;

    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    @Column(name = "free_seats", nullable = false)
    private Integer freeSeats;

    @Enumerated(EnumType.STRING)
    @Column(name = "airline", nullable = false)
    private Airline airline;

    @Enumerated(EnumType.STRING)
    @Column(name = "aircraft_type", nullable = false)
    private AircraftType aircraftType;

    @MapsId
    @OneToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private FlightEntity flight;

    @PrePersist
    private void initializeFreeSeats() {
        this.freeSeats = totalSeats;
    }
}
