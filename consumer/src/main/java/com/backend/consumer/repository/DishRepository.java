package com.backend.consumer.repository;

import com.backend.consumer.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    @Query(
            nativeQuery = true,
            value="select * from dish where restaurant_id = :restaurantId and is_deleted=0"
    )
    List<Dish> getDishListFromRestaurantId(@Param("restaurantId") Long restaurantId);

    @Query(
            nativeQuery = true,
            value = "select * from dish where dish_id = :dishId and is_deleted=0"
    )
    Optional<Dish> getDishDetailFromDishId(@Param("dishId")Long dishId);

    @Query(
            nativeQuery = true,
            value="select * from dish where dish_id = :dishId and is_deleted=0"

    )
    Optional<Dish>findNotDeletedDishByDishId(@Param("dishId")Long dishId);
}
