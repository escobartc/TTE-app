package com.challenge.tteapp.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class WishListResponse {

    @JsonProperty("user_id")
    private String userId;
    private List<Integer> wishlist;
}
