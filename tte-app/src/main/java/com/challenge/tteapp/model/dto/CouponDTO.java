package com.challenge.tteapp.model.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CouponDTO {
    @Size(min = 5)
    private String coupon_code;
    @Min(value = 1)
    @Max(value = 99)
    private Integer discount_percentage;
}
