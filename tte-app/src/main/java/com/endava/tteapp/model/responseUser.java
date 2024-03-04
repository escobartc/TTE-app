package com.endava.tteapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class responseUser {

    @JsonProperty
    private String id = "1212321";
    @JsonProperty
    private String email;
    @JsonProperty
    private String username;
    @JsonProperty
    private String role;

}
