package com.backend.consumer.service;

import com.backend.consumer.entity.ConsumerJwtToken;
import com.backend.consumer.repository.ConsumerJwtTokenRepository;
import com.backend.consumer.repository.ConsumerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final ConsumerJwtTokenRepository consumerJwtTokenRepository;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");


        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }

        String jwtToken = authHeader.substring(7);
        Optional<ConsumerJwtToken> consumerJwtToken = consumerJwtTokenRepository.findByJwtToken(jwtToken);

        if(consumerJwtToken.isEmpty()){
            return;
        }

        consumerJwtToken.get().setExpired(true);
        consumerJwtToken.get().setRevoked(true);
        consumerJwtTokenRepository.save(consumerJwtToken.get());

        return;

    }
}
