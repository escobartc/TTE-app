package com.endava.tteapp.repository;

import com.endava.tteapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminRepository extends JpaRepository<User,Long> {
    @Query("SELECT u FROM User u WHERE u.email = :parameter OR u.username = :parameter")
    User findElement(@Param("parameter") String parameter);
}
