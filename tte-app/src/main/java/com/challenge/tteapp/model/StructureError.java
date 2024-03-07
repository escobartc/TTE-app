package com.challenge.tteapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;

@Setter
public class StructureError {

    @JsonProperty
    private ErrorModel error;

}
