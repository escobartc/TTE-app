package com.challenge.tteapp.repository;

import com.challenge.tteapp.model.Cart;
import com.challenge.tteapp.model.Orders;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT o FROM Orders o WHERE o.user = :userId AND o.orderStatus = :status")
    Orders findOrders(@Param("userId") Long userId, @Param("status") String status);

    @Query("SELECT o FROM Orders o WHERE o.id = :idOrder")
    Orders findOrdersId(@Param("idOrder") Long idOrder);
    @Query("SELECT o FROM Orders o WHERE o.user = :idUser")
    List<Orders> findOrdersUserId(@Param("idUser") Long idUser);

    @Query("SELECT o FROM Orders o WHERE o.orderStatus <> 'CREATED'")
    List<Orders> findAllOrders();

    @Query(value = "SELECT o.couponId FROM Orders o WHERE o.user  = :userId")
    Long findCouponId(@Param("userId") Long userId);

    @Query("SELECT o.id FROM Orders o WHERE o.user = :userId")
    List<Integer> findArticleIdsByUserId(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Orders o where o.user=:userId")
    void deleteAllByUser(@Param("userId") Long userId);
}
