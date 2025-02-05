package com.urlshortener.repository;

import com.urlshortener.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
   Optional<User> findByresetToken(String resetToken);
}
