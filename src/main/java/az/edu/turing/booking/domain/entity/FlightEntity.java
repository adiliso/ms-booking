package az.edu.turing.booking.domain.entity;

import az.edu.turing.booking.model.enums.City;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "flights")
@EqualsAndHashCode(callSuper = true)
public class FlightEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "origin_point", nullable = false)
    private City originPoint;

    @Enumerated(EnumType.STRING)
    @Column(name = "destination_point", nullable = false)
    private City destinationPoint;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Column(name = "price", nullable = false)
    private Double price;

    @OneToOne(mappedBy = "flight", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private FlightDetailsEntity flightDetails;

    public void setFlightDetails(FlightDetailsEntity flightDetails) {
        if (flightDetails == null) {
            if (this.flightDetails != null) {
                this.flightDetails.setFlight(null);
            }
        } else {
            flightDetails.setFlight(this);
        }
        this.flightDetails = flightDetails;
    }
}
