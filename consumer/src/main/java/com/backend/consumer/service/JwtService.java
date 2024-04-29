package com.backend.consumer.service;

import com.backend.consumer.entity.Consumer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private static final String SECRET_KEY = "7hicK3uaDfz9u1XCLzPUtRLBg6WlEgEMw+LeWR50vse/EgagYTHixpyxWLpda0Cc";

    public String extractConsumerEmailId(String jwtToken) {
        return extractClaim(jwtToken, Claims :: getSubject);
    }

    private <T> T extractClaim(String jwtToken, Function<Claims, T>claimsResolver){
        final Claims allClaims = extractAllClaims(jwtToken);
        return claimsResolver.apply(allClaims);
    }

    private Claims extractAllClaims(String jwtToken){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJwtToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 60 * 24 *30))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateJwtTokenWithoutExtraClaims(UserDetails userDetails){
        return generateJwtToken(new HashMap<>(), userDetails);
    }

    public boolean isJwtTokenValid(String jwtToken, UserDetails userDetails){
        final String consumerEmailId = extractConsumerEmailId(jwtToken);
        return (consumerEmailId.equals(userDetails.getUsername()) && !isJwtTokenExpired(jwtToken));
    }

    private boolean isJwtTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken,Claims :: getExpiration);
    }

}
