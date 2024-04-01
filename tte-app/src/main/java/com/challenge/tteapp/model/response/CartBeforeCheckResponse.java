package com.challenge.tteapp.model.response;

import com.challenge.tteapp.model.Products;
import com.challenge.tteapp.model.dto.CouponDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CartBeforeCheckResponse {
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("shopping_cart")
    private List<Products> shoppingCart;
    @JsonProperty("coupon_applied")
    private CouponDTO couponApplied;
    @JsonProperty("total_before_discount")
    private double totalBeforeDiscount;
    @JsonProperty("total_after_discount")
    private double totalAfterDiscount;
    @JsonProperty("shipping_cost")
    private double shippingCost;
    @JsonProperty("final_total")
    private double finalTotal;

}
