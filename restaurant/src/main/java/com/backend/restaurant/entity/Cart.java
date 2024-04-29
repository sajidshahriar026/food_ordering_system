package com.backend.restaurant.entity;

import com.backend.restaurant.repository.CartRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Cart {
    @Id
    @SequenceGenerator(
            name="cart_sequence",
            sequenceName = "cart_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cart_sequence"
    )
    private Long cartId;

    @ManyToOne
    @JoinColumn(
            name = "dish_id",
            referencedColumnName = "dishId"
    )
    private Dish dish;
    @ManyToOne()
    @JoinColumn(
            name= "consumer_id",
            referencedColumnName = "consumerId"
    )
    private Consumer consumer;

    @ManyToOne()
    @JoinColumn(
            name="order_id",
            referencedColumnName = "orderId"
    )
    private Order order;

    private int quantity;

    private int status = CartRepository.IN_CART_STATUS;

    private LocalDateTime localDateTime;

}
