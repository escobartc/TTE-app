package com.challenge.tteapp.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"state", "id"})
public class CategoryDTO {
    private Long id;
    @NotEmpty(message = "Category name is required")
    private String name;
    private String state;
}
