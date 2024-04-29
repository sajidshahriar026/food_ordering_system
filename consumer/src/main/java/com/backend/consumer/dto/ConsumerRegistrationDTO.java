package com.backend.consumer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsumerRegistrationDTO {
    private String consumerName;
    private String consumerPassword;
    private String consumerEmailId;
}
