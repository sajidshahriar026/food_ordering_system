package com.backend.consumer.repository;

import com.backend.consumer.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    int IN_CART_STATUS = 0;
    int ORDERED_STATUS = 1;
    int DELIVERED_STATUS = 2;
    int CANCELLED_STATUS = 3;
    int DELETED_STATUS=4;

    @Query(
            nativeQuery = true,
            value = "select * from cart where consumer_id=:consumerId and dish_id=:dishId and status = 0"
    )
    Optional<Cart> getActiveCartByDishIdAndConsumerId(@Param("dishId")Long dishId, @Param("consumerId")Long consumerId);

    @Query(
            nativeQuery = true,
            value = "select * from cart c" +
                    " where consumer_id = :consumerId and status = 0"
    )
    List<Cart> getActiveCartByConsumerId(@Param("consumerId")Long consumerId);

    @Query(
            nativeQuery = true,
            value = "select * from cart c " +
                    "where consumer_id=:consumerId and cart_id=:cartId and status = 0"
    )
    Optional<Cart> getActiveCartByCartIdAndConsumerId(@Param("cartId")Long cartId,@Param("consumerId")Long consumerId);
}
