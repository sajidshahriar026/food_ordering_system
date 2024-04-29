package com.backend.consumer.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantDTO {
    private Long restaurantId;

    private String restaurantName;

    private String restaurantAddress;

    private String restaurantEmailId;
}
