package com.challenge.tteapp.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CartDTO {
    private String id;
    private Integer product_id;
    private Integer quantity;
}
