package com.backend.consumer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "tbl_order"
)
public class Order {
    @Id
    @SequenceGenerator(
            name="order_sequence",
            sequenceName = "order_sequence",
            allocationSize = 1

    )
    @GeneratedValue(
            generator = "order_sequence",
            strategy = GenerationType.SEQUENCE
    )
    private Long orderId;

    @ManyToOne(
            optional = false
    )
    @JoinColumn(
            name="consumer_id",
            referencedColumnName = "consumerId"
    )
    private Consumer consumer;
    @Column(
            nullable = false
    )
    private String phoneNumber;

    @Column(
            nullable = false
    )
    private String address;

    @Column(
            nullable = false
    )
    private double totalCost;
    @Column(
            nullable = false
    )
    private LocalDateTime orderTime;


}
