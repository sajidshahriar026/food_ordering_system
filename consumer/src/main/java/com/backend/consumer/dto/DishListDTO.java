package com.backend.consumer.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DishListDTO {
    private Long dishId;
    private String dishName;
    private String dishDescription;
    private double dishPrice;
    private Long restaurantId;
}