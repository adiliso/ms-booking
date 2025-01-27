package az.edu.turing.booking.service;

import az.edu.turing.booking.domain.entity.UserEntity;
import az.edu.turing.booking.model.enums.UserRole;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface UserService {

    void create(UserEntity userEntity);

    void updateUsername(long id, String username);

    void updateRole(long id, UserRole role);

    boolean isAdmin(long id);
}
