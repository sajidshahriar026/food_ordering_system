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
public class ConsumerJwtToken {

    @Id
    @SequenceGenerator(
            name="consumer_token_sequence",
            sequenceName = "consumer_token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "consumer_token_sequence"
    )
    private Long id;

    private String jwtToken;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    private boolean expired;

    private boolean revoked;

    @ManyToOne
    @JoinColumn(
            name="consumer_id",
            referencedColumnName = "consumerId"
    )
    private Consumer consumer;


}
