package com.challenge.tteapp.repository;

import com.challenge.tteapp.model.Shopper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public interface ShopperRepository extends JpaRepository<Shopper,Long> {
    @Query(value = "SELECT s FROM Shopper s WHERE s.username = :parameter OR s.email = :parameter")
    Shopper findElement(@Param("parameter") String parameter);

    Optional<Shopper> findByUsername(String username);
}
