package com.challenge.tteapp.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CouponDTO {
    @Size(min = 5)
    @NotEmpty(message = "coupon_code cannot be empty")
    @JsonProperty("coupon_code")
    private String couponCode;
    @Min(value = 1)
    @Max(value = 99)
    @JsonProperty("discount_percentage")
    private Integer discountPercentage;
}
