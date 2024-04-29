package com.backend.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RestaurantRegistrationDTO {
    private String restaurantName;

    private String restaurantAddress;
    private String restaurantEmailId;

    private String restaurantPassword;
}
