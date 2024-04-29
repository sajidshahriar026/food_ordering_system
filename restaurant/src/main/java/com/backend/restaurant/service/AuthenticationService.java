package com.backend.restaurant.service;

import com.backend.restaurant.dto.AuthenticationResponse;
import com.backend.restaurant.dto.RestaurantLoginDTO;
import com.backend.restaurant.dto.RestaurantRegistrationDTO;
import com.backend.restaurant.entity.Restaurant;
import com.backend.restaurant.entity.RestaurantJwtToken;
import com.backend.restaurant.entity.Role;
import com.backend.restaurant.entity.TokenType;
import com.backend.restaurant.repository.RestaurantJwtTokenRepository;
import com.backend.restaurant.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RestaurantJwtTokenRepository restaurantJwtTokenRepository;

    @Autowired
    private AuthenticationManager authenticationManager;



    public AuthenticationResponse registerRestaurant(RestaurantRegistrationDTO restaurantRegistrationDTO) {
        Optional<Restaurant> restaurantByEmailId =
                restaurantRepository.findByRestaurantEmailIdIgnoreCase(restaurantRegistrationDTO.getRestaurantEmailId());
        Optional<Restaurant> restaurantByName =
                restaurantRepository.findByRestaurantNameIgnoreCase(restaurantRegistrationDTO.getRestaurantName());

        if(!restaurantByEmailId.isPresent() && !restaurantByName.isPresent()){
            Restaurant restaurant = Restaurant.builder()
                    .restaurantName(restaurantRegistrationDTO.getRestaurantName())
                    .restaurantEmailId(restaurantRegistrationDTO.getRestaurantEmailId())
                    .restaurantPassword(passwordEncoder.encode(restaurantRegistrationDTO.getRestaurantPassword()))
                    .restaurantAddress(restaurantRegistrationDTO.getRestaurantAddress())
                    .role(Role.RESTAURANT)
                    .build();
            restaurantRepository.save(restaurant);

            String jwtToken = jwtService.generateJwtTokenWithoutExtraClaims(restaurant);

            revokeAllPreviousRestaurantJwtToken(restaurant);

            saveRestaurantJwtToken(restaurant, jwtToken);

            return AuthenticationResponse.builder()
                    .jwtToken(jwtToken)
                    .build();

        }
        else{
            return null;
        }
    }

    public AuthenticationResponse loginRestaurant(RestaurantLoginDTO restaurantLoginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                      restaurantLoginDTO.getRestaurantEmailId(),
                      restaurantLoginDTO.getRestaurantPassword()
                )
        );

        Optional<Restaurant> restaurant = restaurantRepository.findByRestaurantEmailIdIgnoreCase(restaurantLoginDTO.getRestaurantEmailId());

        if(restaurant.isEmpty()){
            return null;
        }

        String jwtToken = jwtService.generateJwtTokenWithoutExtraClaims(restaurant.get());

        revokeAllPreviousRestaurantJwtToken(restaurant.get());

        saveRestaurantJwtToken(restaurant.get(),jwtToken);


        return AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .build();
    }


    private void revokeAllPreviousRestaurantJwtToken(Restaurant restaurant){
        List<RestaurantJwtToken> validRestaurantJwtTokenList= restaurantJwtTokenRepository.findAllValidTokensByRestaurantId(restaurant.getRestaurantId());

        if(validRestaurantJwtTokenList.isEmpty()){
            return ;
        }

        for(RestaurantJwtToken validRestaurantJwtToken : validRestaurantJwtTokenList){
            validRestaurantJwtToken.setRevoked(true);
            validRestaurantJwtToken.setExpired(true);
        }
        restaurantJwtTokenRepository.saveAll(validRestaurantJwtTokenList);
    }

    private void saveRestaurantJwtToken(Restaurant restaurant, String jwtToken){
        RestaurantJwtToken restaurantJwtToken = RestaurantJwtToken.builder()
                .jwtToken(jwtToken)
                .expired(false)
                .revoked(false)
                .restaurant(restaurant)
                .tokenType(TokenType.BEARER)
                .build();

        restaurantJwtTokenRepository.save(restaurantJwtToken);
    }
}
