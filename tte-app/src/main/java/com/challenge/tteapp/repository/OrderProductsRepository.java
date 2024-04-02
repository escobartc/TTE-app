package com.challenge.tteapp.repository;

import com.challenge.tteapp.model.OrderProducts;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderProductsRepository extends JpaRepository<OrderProducts, Long> {
    @Query("SELECT o FROM OrderProducts o WHERE o.orderId = :idOrder")
    List<OrderProducts> findAllOrderProducts(@Param("idOrder") Long idOrder);

    @Transactional
    @Modifying
    @Query("DELETE FROM OrderProducts o where o.orderId=:orderId")
    void deleteAllByUser(@Param("orderId") Long orderId);
}
