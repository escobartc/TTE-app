package com.challenge.tteapp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class WishListDTO {
    @JsonProperty("user_id")
    private String userId;
}
