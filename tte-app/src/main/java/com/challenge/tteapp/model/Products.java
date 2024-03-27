package com.challenge.tteapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Products {
    @JsonProperty("product_id")
    private Long productId;
    private Integer quantity;
}
