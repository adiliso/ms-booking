package az.edu.turing.booking.domain.repository;

import az.edu.turing.booking.domain.entity.BookingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    boolean existsByIdAndCreatedByIs(Long bookingId, Long createdBy);

    Page<BookingEntity> findAllByUsersUsername(String username, Pageable pageable);
}
