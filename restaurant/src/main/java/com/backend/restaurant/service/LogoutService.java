package com.backend.restaurant.service;

import com.backend.restaurant.entity.RestaurantJwtToken;
import com.backend.restaurant.repository.RestaurantJwtTokenRepository;
import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService  implements LogoutHandler {

    private final RestaurantJwtTokenRepository restaurantJwtTokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        final String authHeader = request.getHeader("Authorization");


        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }

        String jwtToken = authHeader.substring(7);
        Optional<RestaurantJwtToken> restaurantJwtToken = restaurantJwtTokenRepository.findByJwtToken(jwtToken);

        if(restaurantJwtToken.isEmpty()){
            return;
        }

        restaurantJwtToken.get().setExpired(true);
        restaurantJwtToken.get().setRevoked(true);
        restaurantJwtTokenRepository.save(restaurantJwtToken.get());

        return;

    }
}
