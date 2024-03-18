package com.challenge.tteapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorModel {

    @JsonProperty("message")
    private String message;

    @JsonProperty("code")
    private String code;
}
