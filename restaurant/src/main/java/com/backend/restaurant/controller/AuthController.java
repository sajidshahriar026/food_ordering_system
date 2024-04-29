package com.backend.restaurant.controller;

import com.backend.restaurant.dto.AuthenticationResponse;
import com.backend.restaurant.dto.RestaurantLoginDTO;
import com.backend.restaurant.dto.RestaurantRegistrationDTO;
import com.backend.restaurant.entity.Restaurant;
import com.backend.restaurant.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController{
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerRestaurant(@RequestBody RestaurantRegistrationDTO restaurantRegistrationDTO){
        return ResponseEntity.ok(authenticationService.registerRestaurant(restaurantRegistrationDTO));
    }

    @PostMapping("/login")
    ResponseEntity<AuthenticationResponse> loginRestaurant(@RequestBody RestaurantLoginDTO restaurantLoginDTO){
        return ResponseEntity.ok(authenticationService.loginRestaurant(restaurantLoginDTO));
    }


}
