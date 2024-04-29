package com.backend.consumer.service;

import com.backend.consumer.dto.AuthenticationResponse;
import com.backend.consumer.dto.ConsumerLoginDTO;
import com.backend.consumer.dto.ConsumerRegistrationDTO;
import com.backend.consumer.entity.Consumer;

import java.util.List;
import java.util.Optional;

import com.backend.consumer.entity.ConsumerJwtToken;
import com.backend.consumer.entity.Role;
import com.backend.consumer.entity.TokenType;
import com.backend.consumer.repository.ConsumerJwtTokenRepository;
import com.backend.consumer.repository.ConsumerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private ConsumerRepository consumerRepository;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ConsumerJwtTokenRepository consumerJwtTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;
    public AuthenticationResponse registerConsumer(ConsumerRegistrationDTO consumerRegistrationDTO) {
        Optional<Consumer> consumerByEmailId =
                consumerRepository.findByConsumerEmailIdIgnoreCase(consumerRegistrationDTO.getConsumerEmailId());

        if(consumerByEmailId.isPresent()){
            return null;
        }

        Consumer consumer = Consumer.builder()
                .consumerName(consumerRegistrationDTO.getConsumerName())
                .consumerEmailId(consumerRegistrationDTO.getConsumerEmailId())
                .consumerPassword(passwordEncoder.encode(consumerRegistrationDTO.getConsumerPassword()))
                .role(Role.CONSUMER)
                .build();

        consumerRepository.save(consumer);

        String jwtToken = jwtService.generateJwtTokenWithoutExtraClaims(consumer);

        revokeAllPreviousConsumerJwtToken(consumer);

        saveConsumerJwtToken(consumer, jwtToken);

        return AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .build();

    }

    public AuthenticationResponse loginConsumer(ConsumerLoginDTO consumerLoginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        consumerLoginDTO.getConsumerEmailId(),
                        consumerLoginDTO.getConsumerPassword()
                )
        );

        Optional<Consumer> consumer = consumerRepository.findByConsumerEmailIdIgnoreCase(consumerLoginDTO.getConsumerEmailId());

        if(consumer.isEmpty()){
            return null;
        }

        String jwtToken = jwtService.generateJwtTokenWithoutExtraClaims(consumer.get());

        revokeAllPreviousConsumerJwtToken(consumer.get());

        saveConsumerJwtToken(consumer.get(),jwtToken);


        return AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .build();
    }



    private void revokeAllPreviousConsumerJwtToken(Consumer consumer) {
        List<ConsumerJwtToken> validConsumerJwtTokenList = consumerJwtTokenRepository.findAllValidTokensByConsumerId(consumer.getConsumerId());

        if(validConsumerJwtTokenList.isEmpty()){
            return;
        }

        for(ConsumerJwtToken validConsumerJwtToken : validConsumerJwtTokenList){
            validConsumerJwtToken.setRevoked(true);
            validConsumerJwtToken.setExpired(true);
        }

    }

    private void saveConsumerJwtToken(Consumer consumer, String jwtToken){
        ConsumerJwtToken consumerJwtToken = ConsumerJwtToken.builder()
                .jwtToken(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .consumer(consumer)
                .build();
        consumerJwtTokenRepository.save(consumerJwtToken);
    }
}
