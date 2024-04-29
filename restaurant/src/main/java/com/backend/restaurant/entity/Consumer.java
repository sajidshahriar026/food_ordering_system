package com.backend.restaurant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consumer {
    @Id
    @SequenceGenerator(
            name = "consumer_sequence",
            sequenceName ="consumer_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "consumer_sequence"
    )
    private Long consumerId;

    @Column(
            nullable = false
    )
    private String consumerName;

    @Column(
            nullable = false,
            unique = true
    )
    private String consumerEmailId;
    @Column(
            nullable = false
    )
    private String consumerPassword;
}

