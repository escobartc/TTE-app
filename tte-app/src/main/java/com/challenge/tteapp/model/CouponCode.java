package com.challenge.tteapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CouponCode {
@JsonProperty("coupon_code")
private String couponCode;

}
