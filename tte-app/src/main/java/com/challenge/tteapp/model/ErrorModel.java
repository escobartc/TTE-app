package com.challenge.tteapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorModel {

    @JsonProperty("code")
    private int code;

    @JsonProperty("message")
    private String message;

}
