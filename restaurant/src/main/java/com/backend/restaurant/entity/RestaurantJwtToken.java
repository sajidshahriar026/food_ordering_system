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
public class RestaurantJwtToken {

    @Id
    @SequenceGenerator(
            name="restaurant_token_sequence",
            sequenceName = "restaurant_token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "restaurant_token_sequence"
    )
    private Long id;

    private String jwtToken;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    private boolean expired;

    private boolean revoked;

    @ManyToOne
    @JoinColumn(
            name="restaurant_id",
            referencedColumnName = "restaurantId"
    )
    private Restaurant restaurant;


}
