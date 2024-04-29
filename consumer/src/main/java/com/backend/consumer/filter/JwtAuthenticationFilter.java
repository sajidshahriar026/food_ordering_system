package com.backend.consumer.filter;

import com.backend.consumer.repository.ConsumerJwtTokenRepository;
import com.backend.consumer.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ConsumerJwtTokenRepository consumerJwtTokenRepository;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {


        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String consumerEmailId;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //if jwt token if available

        jwtToken = authHeader.substring(7);

        consumerEmailId = jwtService.extractConsumerEmailId(jwtToken);

        if(consumerEmailId != null && SecurityContextHolder.getContext().getAuthentication() == null){
             UserDetails consumer = this.userDetailsService.loadUserByUsername(consumerEmailId);

             boolean isConsumerJwtTokenValid = consumerJwtTokenRepository.findByJwtToken(jwtToken)
                     .map(token -> !token.isExpired() && !token.isRevoked())
                     .orElse(false);


             if(jwtService.isJwtTokenValid(jwtToken, consumer) && isConsumerJwtTokenValid){
                 UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                         consumer,
                         null,
                         consumer.getAuthorities()
                 );

                 authToken.setDetails(
                         new WebAuthenticationDetailsSource().buildDetails(request)
                 );
                 SecurityContextHolder.getContext().setAuthentication(authToken);
             }


        }
        filterChain.doFilter(request,response);

    }
}
