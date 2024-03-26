package com.challenge.tteapp.repository;

import com.challenge.tteapp.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CouponRepository extends JpaRepository<Coupon,Long> {

    @Query("SELECT couponCode FROM Coupon")
    List<String> findNameCoupon();

    @Query("SELECT c FROM Coupon c WHERE c.couponCode = :parameter")
    Coupon findCoupon(@Param("parameter") String parameter);

}
