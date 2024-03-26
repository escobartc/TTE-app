package com.challenge.tteapp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CouponDelete {
    @NotEmpty(message = "name_coupon cannot be empty")
    @JsonProperty("name_coupon")
    private String name;
}
