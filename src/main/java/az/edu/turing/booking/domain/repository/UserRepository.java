package az.edu.turing.booking.domain.repository;

import az.edu.turing.booking.domain.entity.UserEntity;
import az.edu.turing.booking.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByIdAndRole(Long id, UserRole role);
}
