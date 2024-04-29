package com.backend.restaurant.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "eLUig6fJK2pvHakfBfGK9RfN4R9pMy3miUCX2/q3uxQvQJ4T1wumJ1hgR8VHxI0f";
    public String extractRestaurantEmailId(String jwtToken) {

        return extractClaim(jwtToken, Claims :: getSubject);
    }

    public <T> T extractClaim(String jwtToken, Function<Claims,T> claimResolver){
        final Claims allClaims = extractAllClaims(jwtToken);
        return claimResolver.apply(allClaims);
    }

    private Claims extractAllClaims(String jwtToken){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private Key getSigningKey(){
        //we want to decode it on base64
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJwtToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()  + 1000L *60 *60 *60 *24 * 30))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public String generateJwtTokenWithoutExtraClaims(UserDetails userDetails){
        return generateJwtToken(new HashMap<>(), userDetails);
    }

    public boolean isJwtTokenValid(String jwtToken, UserDetails userDetails){
        final String restaurantEmailId = extractRestaurantEmailId(jwtToken);
        return (restaurantEmailId.equals(userDetails.getUsername()) && !isJwtTokenExpired(jwtToken));
    }

    private boolean isJwtTokenExpired(String jwtToken){
        return extractExpiration(jwtToken).before(new Date());
    }

    private Date extractExpiration(String jwtToken){
        return extractClaim(jwtToken, Claims :: getExpiration);
    }

}
