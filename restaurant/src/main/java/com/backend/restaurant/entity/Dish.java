package com.backend.restaurant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dish {
    @SequenceGenerator(
            name="dish_sequence",
            sequenceName = "dish_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "dish_sequence"
    )
    @Id
    private Long dishId;

    @Column(
            nullable = false
    )
    private String dishName;

    @Column(
            length = 1000
    )
    private String dishDescription;
    private String dishImageUrl;
    @Column(
            nullable = false
    )
    private double dishPrice;
    @ManyToOne
    @JoinColumn(
            name = "restaurant_id",
            referencedColumnName = "restaurantId"
    )
    private Restaurant restaurant;
    private boolean isDeleted = false;


}
