package com.challenge.tteapp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CartDTO {
    @JsonProperty("product_id")
    private Integer productId;
    @Min(value = 1)
    private Integer quantity;
}
