package com.backend.consumer.service;

import com.backend.consumer.entity.Consumer;
import com.backend.consumer.repository.ConsumerRepository;
import com.backend.consumer.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConsumerExtractionService {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private ConsumerRepository consumerRepository;


    public Long getConsumerIdFromAuthHeader(String authHeader){
        String jwtToken = authHeader.substring(7);
        String consumerEmailId = jwtService.extractConsumerEmailId(jwtToken);

        Optional<Consumer> consumerByConsumerEmailId = consumerRepository.findByConsumerEmailIdIgnoreCase(consumerEmailId);

        if(consumerByConsumerEmailId.isEmpty()){
            return null;
        }
        else{
            return consumerByConsumerEmailId.get().getConsumerId();
        }
    }
}
