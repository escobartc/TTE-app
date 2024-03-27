package com.challenge.tteapp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateStatusOrderDTO {

    @JsonProperty("id_order")
    @Min(value = 1)
    private Long idOrder;
    private String status;
}
