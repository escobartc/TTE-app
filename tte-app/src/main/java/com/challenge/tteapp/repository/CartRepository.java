package com.challenge.tteapp.repository;

import com.challenge.tteapp.model.Cart;
import com.challenge.tteapp.model.CartResponse;
import com.challenge.tteapp.model.Products;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    @Query("SELECT c.cartProduct FROM Cart c WHERE c.user = :userId")
    List<Integer> findArticleIdsByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT c.coupon_id  FROM cart c WHERE c.user_id  = :userId",nativeQuery = true)
    List<Long>  AllCart(@Param("userId") Long userId);

    @Query(value = "SELECT c.product_cart AS product_cart, c.quantity AS quantity FROM cart c WHERE c.user_id = :userId", nativeQuery = true)
    List<Object[]> findProductsCartById(@Param("userId") Long userId);
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO cart (user_id, product_cart, quantity) VALUES (:userId, :articleId, :quantity)", nativeQuery = true)
    void addElementToList(@Param("userId") Long userId, @Param("articleId") Integer productId,@Param("quantity") Integer quantity);

    @Transactional
    @Modifying
    @Query(value = "UPDATE cart SET cart.coupon_id = :CouponId WHERE cart.user_id  = :userId",nativeQuery = true)
    void updateCartCoupon(@Param("CouponId") Long CouponId, @Param("userId") Long userId);

}
