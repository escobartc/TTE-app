package com.challenge.tteapp.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryUpdate {
    private Long id;
    @NotEmpty(message = "Category name is required")
    private String name;
}
