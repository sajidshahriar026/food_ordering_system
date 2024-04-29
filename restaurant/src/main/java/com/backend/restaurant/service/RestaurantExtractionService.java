package com.backend.restaurant.service;

import com.backend.restaurant.entity.Restaurant;
import com.backend.restaurant.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RestaurantExtractionService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RestaurantRepository restaurantRepository;

    public Long getRestaurantIdFromAuthHeader(String authHeader){
        String jwtToken = authHeader.substring(7);
        String restaurantEmailId = jwtService.extractRestaurantEmailId(jwtToken);

        Optional<Restaurant> restaurantByRestaurantEmailId = restaurantRepository.findByRestaurantEmailIdIgnoreCase(restaurantEmailId);

        if(restaurantByRestaurantEmailId.isEmpty()){
            return null;
        }
        else{
            return restaurantByRestaurantEmailId.get().getRestaurantId();
        }
    }
}
