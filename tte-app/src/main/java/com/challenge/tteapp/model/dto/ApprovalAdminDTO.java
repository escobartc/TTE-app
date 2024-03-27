package com.challenge.tteapp.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApprovalAdminDTO {

    @Min(value = 1)
    private Long id;
    @NotEmpty(message = "action cannot be empty")
    private String action;
}
