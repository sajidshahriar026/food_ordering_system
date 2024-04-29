package com.backend.restaurant.filter;


import com.backend.restaurant.entity.RestaurantJwtToken;
import com.backend.restaurant.repository.RestaurantJwtTokenRepository;
import com.backend.restaurant.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor //creates constructor for final field of this class
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final RestaurantJwtTokenRepository restaurantJwtTokenRepository;

    @Override
    protected void doFilterInternal(
          @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String restaurantEmailId;

        //if there auth header is null or auth header does not have any jwt token
        //pass the request to next filter
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        //if jwt token is available
        jwtToken = authHeader.substring(7);
        //get the restaurant email id
        restaurantEmailId = jwtService.extractRestaurantEmailId(jwtToken);
        //if email is found in the claim and the security context authentication is null then do validation
        if(restaurantEmailId != null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails restaurant = this.userDetailsService.loadUserByUsername(restaurantEmailId);
            //check if the token is valid or not
            boolean isRestaurantJwtTokenValid = restaurantJwtTokenRepository.findByJwtToken(jwtToken)
                    .map(token -> !token.isExpired() && !token.isRevoked())
                    .orElse(false);


            if(jwtService.isJwtTokenValid(jwtToken, restaurant) && isRestaurantJwtTokenValid){
                //security context needs this to update the security context
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        restaurant,
                        null, //we don't have credentials
                        restaurant.getAuthorities()
                );
                //give some more details to auth token
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                //
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }

        filterChain.doFilter(request,response);

    }
}
