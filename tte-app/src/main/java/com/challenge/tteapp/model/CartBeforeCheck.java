package com.challenge.tteapp.model;

import com.challenge.tteapp.model.dto.CouponDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CartBeforeCheck {
    private String user_id;
    private List<Products> shopping_cart;
    private CouponDTO coupon_applied;
    private int total_before_discount;
    private int total_after_discount;
    private int shipping_cost;
    private int final_total;

}
