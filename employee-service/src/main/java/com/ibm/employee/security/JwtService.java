package com.ibm.employee.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey signingKey;

    @PostConstruct
    public void init() {
        signingKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Parse JWT and return all claims
     */
    public Claims extractClaims(String token) {

        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extract username
     */
    public String extractUsername(String token) {

        return extractClaims(token).getSubject();
    }

    /**
     * Extract roles
     */
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        List<String> roles = extractClaims(token).get("roles", List.class);
        return roles == null ? List.of() : roles;
    }

    /**
     * Check expiration
     */
    public boolean isTokenExpired(String token) {

        Date expiration = extractClaims(token).getExpiration();

        return expiration.before(new Date());
    }

    /**
     * Validate JWT
     */
    public boolean isValid(String token) {

        try {

            Claims claims = extractClaims(token);

            return claims.getExpiration()
                    .after(new Date());

        } catch (JwtException | IllegalArgumentException ex) {

            System.out.println(ex.getClass().getName());
            System.out.println(ex.getMessage());
            return false;
        }
    }

}