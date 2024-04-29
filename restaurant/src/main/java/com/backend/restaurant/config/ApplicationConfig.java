package com.backend.restaurant.config;

import com.backend.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final RestaurantRepository restaurantRepository;
    @Bean
    public UserDetailsService userDetailsService(){
        return restaurantEmailId -> restaurantRepository.findByRestaurantEmailIdIgnoreCase(restaurantEmailId)
                .orElseThrow(()-> new UsernameNotFoundException("user not found"));
    }

    @Bean
    public AuthenticationProvider autheticationProvider(){
        //authenticationProvider is the data access object
        //which is responsible fetching user details and encode password
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        //from where the user data in this case restaurant data is going to be fetched
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;

    }

    //manages authentication
    //has a bunch of methods
    //one of them allow us to authenticate user using username and password
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws  Exception{
        //authentication configuration holds info about authentication manager
        return config.getAuthenticationManager();
    }

    //specifies password encoding method
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
