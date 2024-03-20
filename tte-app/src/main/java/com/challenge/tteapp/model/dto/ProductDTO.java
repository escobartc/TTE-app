package com.challenge.tteapp.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String title;
    private BigDecimal price;
    private String description;
    private CategoryDTO category;
    private String image;
    private String state;
    private RatingDTO rating;
    private InventoryDTO inventory;
    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", image='" + image + '\'' +
                ", state='" + state + '\'' +
                ", rating=" + rating +
                ", inventory=" + inventory +
                '}';
    }
}
