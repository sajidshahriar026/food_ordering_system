package com.backend.consumer.entity;

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
public class Restaurant {
    @Id
    @SequenceGenerator(
            name="restaurant_sequence",
            sequenceName = "restaurant_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "restaurant_sequence"
    )
    private Long restaurantId;
    @Column(
            nullable = false,
            unique = true
    )
    private String restaurantName;

    private String restaurantAddress;
    @Column(
            nullable = false,
            unique = true
    )
    private String restaurantEmailId;
    @Column(
            nullable = false
    )
    private String restaurantPassword;

    @Enumerated(EnumType.STRING)
    private Role role;
}