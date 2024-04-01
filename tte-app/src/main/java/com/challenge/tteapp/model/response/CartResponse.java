package com.challenge.tteapp.model.response;

import com.challenge.tteapp.model.Products;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CartResponse {

    @JsonProperty("user_id")
    private String userId;
    private List<Products> products;
}


