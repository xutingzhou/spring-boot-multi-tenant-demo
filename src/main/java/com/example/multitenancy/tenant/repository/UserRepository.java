package com.example.multitenancy.tenant.repository;

import com.example.multitenancy.tenant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Xutingzhou
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(long id);

}
