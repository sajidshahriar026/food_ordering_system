package com.backend.consumer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DishDetailDTO {
    private Long dishId;
    private String dishName;
    private String dishDescription;
    private String dishImageUrl;
    private double dishPrice;
    private Long restaurantID;

}
