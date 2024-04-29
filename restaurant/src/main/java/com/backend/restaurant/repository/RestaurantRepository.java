package com.backend.restaurant.repository;

import com.backend.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findByRestaurantNameIgnoreCase(String restaurantName);

    Optional<Restaurant> findByRestaurantEmailIdIgnoreCase(String restaurantEmailId);
}
