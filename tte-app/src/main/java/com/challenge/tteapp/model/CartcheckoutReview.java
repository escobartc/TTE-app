package com.challenge.tteapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@JsonPropertyOrder
public class CartcheckoutReview {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("order_id")
    private Long orderId;
    private List<Products> products;
    private String status;
}
