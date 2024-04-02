package com.challenge.tteapp.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Long productId;
    @NotEmpty(message = "user cannot be empty")
    private String user;
    @NotEmpty(message = "comment cannot be empty")
    private String comment;
}
