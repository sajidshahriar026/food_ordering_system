package com.backend.consumer.controller;

import com.backend.consumer.dto.AuthenticationResponse;
import com.backend.consumer.dto.ConsumerLoginDTO;
import com.backend.consumer.dto.ConsumerRegistrationDTO;
import com.backend.consumer.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController{

    @Autowired
    private AuthenticationService autheticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse>registerConsumer(@RequestBody ConsumerRegistrationDTO consumerRegistrationDTO){
        return ResponseEntity.ok(autheticationService.registerConsumer(consumerRegistrationDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse>loginConsumer(@RequestBody ConsumerLoginDTO consumerLoginDTO){
        return ResponseEntity.ok(autheticationService.loginConsumer(consumerLoginDTO));
    }

}
