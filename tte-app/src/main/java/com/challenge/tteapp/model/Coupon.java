package com.challenge.tteapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "coupon")
@Getter
@Setter
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_coupon")
    private String couponCode;
    @Column(name = "percentage")
    private Integer discountPercentage;
    @Column(name = "use_coupon")
    private Boolean useCoupon;

}