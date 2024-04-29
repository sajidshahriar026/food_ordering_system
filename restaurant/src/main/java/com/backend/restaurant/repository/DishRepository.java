package com.backend.restaurant.repository;

import com.backend.restaurant.entity.Dish;
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
            value = "select * from dish where dish.dish_name = :dishName and dish.restaurant_id = :restaurantId and is_deleted=0"
    )
    Optional<Dish> findByDishNameAndRestaurantId(@Param("dishName")String dishName, @Param("restaurantId")Long restaurantId);

    @Query(
            nativeQuery = true,
            value="select * from dish where restaurant_id = :restaurantId and is_deleted = 0"
    )
    List<Dish> getDishListByRestaurantId(@Param("restaurantId")Long restaurantId);

    @Query(
            nativeQuery = true,
            value = "select * from dish where dish_id=:dishId and restaurant_id=:restaurantId and is_deleted=0"
    )
    Optional<Dish> findByDishIdAndRestaurantId(@Param("restaurantId")Long restaurantId,@Param("dishId")Long dishId);
}
