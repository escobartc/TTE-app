package com.challenge.tteapp.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class CartResponse {

    private String user_id;
    private List<Products> products;
}


