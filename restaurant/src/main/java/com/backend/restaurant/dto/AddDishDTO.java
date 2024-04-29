package com.backend.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddDishDTO {
    private String dishName;
    private String dishDescription;
    private String dishImageUrl;
    private double dishPrice;
}
