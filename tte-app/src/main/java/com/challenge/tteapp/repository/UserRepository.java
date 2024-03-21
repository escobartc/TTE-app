package com.challenge.tteapp.repository;

import com.challenge.tteapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @Query("SELECT u FROM User u WHERE u.email = :parameter OR u.username = :parameter")
    User findElement(@Param("parameter") String parameter);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String user);
}
