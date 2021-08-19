package com.example.multitenancy.repository;

import com.example.multitenancy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Xutingzhou
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(long id);

}
