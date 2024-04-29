package com.backend.restaurant.repository;

import com.backend.restaurant.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    int IN_CART_STATUS = 0;
    int ORDERED_STATUS = 1;
    int DELIVERED_STATUS = 2;
    int CANCELLED_STATUS = 3;
    int DELETED_STATUS=4;

    @Query(
            nativeQuery = true,
            value = "select * from cart " +
                    "where dish_id = :dishId " +
                    "and (status = 0 or status = 1)"
    )
    List<Cart> getOrderedAndActiveCartByDishId(@Param("dishId")Long dishId);

    @Query(
            nativeQuery = true,
            value = "select * from cart where " +
                    "status = 1 and dish_id " +
                    "in (select dish_id from dish where restaurant_id = :restaurantId and is_deleted = 0)"
    )
    List<Cart> getOrderListByRestaurantId(@Param("restaurantId")Long restaurantId);
}
