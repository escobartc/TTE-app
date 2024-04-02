package com.challenge.tteapp.repository;

import com.challenge.tteapp.model.Cart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("SqlNoDataSourceInspection")
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c.cartProduct FROM Cart c WHERE c.user = :userId")
    List<Integer> findArticleIdsByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Cart c WHERE c.user = :userId")
    void deleteElementsByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT c.product_id, c.quantity  FROM cart c WHERE c.user_id = :userId", nativeQuery = true)
    List<Object[]> findProductsCartById(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO cart (user_id, product_id, quantity) VALUES (:userId, :articleId, :quantity)", nativeQuery = true)
    void addElementToList(@Param("userId") Long userId, @Param("articleId") Integer articleId, @Param("quantity") Integer quantity);

    @Transactional
    @Modifying
    @Query(value = "UPDATE cart SET cart.quantity = :quantity WHERE cart.user_id  = :userId", nativeQuery = true)
    void updateCartQuantity(@Param("quantity") Integer quantity, @Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Cart c where c.user=:userId")
    void deleteAllByUser(@Param("userId") Long userId);
}
