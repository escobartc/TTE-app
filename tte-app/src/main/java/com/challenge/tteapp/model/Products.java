package com.challenge.tteapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Products {
    private Long productCart;
    private Long quantity;
}
