package com.challenge.tteapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "cart")
@Getter
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long user;

    @Column(name = "product_cart")
    private Integer cartProduct;

    private Integer quantity;

    @Column(name = "coupon_id")
    private String couponId;
}
