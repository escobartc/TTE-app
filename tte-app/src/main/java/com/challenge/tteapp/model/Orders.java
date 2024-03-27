package com.challenge.tteapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long user;

    @Column(name = "order_status")
    private String orderStatus;

    @Column(name = "coupon_id")
    private Long couponId;
}
