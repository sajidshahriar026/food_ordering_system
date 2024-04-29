package com.backend.consumer.dto;

import com.backend.consumer.entity.Dish;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartDTO {
    private Long cartId;
    private Long dishId;
    private String dishName;
    private double totalPrice;
    private int quantity;
    private Long consumerId;


}
