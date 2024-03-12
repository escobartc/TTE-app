package com.challenge.tteapp.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"state"}) // Exclude the "state" property from serialization
public class CategoryDTO {
    private Long id;
    private String name;
    private String state;
}