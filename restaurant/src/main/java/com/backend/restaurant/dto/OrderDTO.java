package com.backend.restaurant.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDTO{
    private Long cartId;
    private Long orderId;
    private String consumerName;
    private String dishName;
    private int quantity;
    private double totalEarnings;

}
