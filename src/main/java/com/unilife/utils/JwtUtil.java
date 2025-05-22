package com.unilife.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// @Component
public class JwtUtil {

    // @Value("${jwt.secret}")
    private String secret = "yourSecretKeyForJWTSigningItShouldBeLongAndSecure"; // Default or fetched from properties

    // @Value("${jwt.expiration.ms}")
    private long expirationMs = 86400000; // Default or fetched from properties

    private SecretKey getSigningKey() {
        // Ensure the secret key is long enough for HS256, HS384, or HS512
        // For HS256, the key must be at least 256 bits (32 bytes)
        // If your secret is shorter, you might need to derive a key or use a stronger one.
        // This is a simplified key generation. In production, manage keys securely.
        byte[] keyBytes = secret.getBytes();
        if (keyBytes.length < 32) {
            // Pad or hash the key to meet the length requirement safely.
            // This is a simplistic approach; consider more robust key derivation in production.
            byte[] newKeyBytes = new byte[32];
            System.arraycopy(keyBytes, 0, newKeyBytes, 0, Math.min(keyBytes.length, 32));
            keyBytes = newKeyBytes;
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Generate token for user
    public String generateToken(Object /*UserDetails*/ userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Add any custom claims if needed
        // claims.put("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        // String username = userDetails.getUsername();
        String username = "testUser"; // Placeholder for UserDetails
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Validate token
    public Boolean validateToken(String token, Object /*UserDetails*/ userDetails) {
        final String username = extractUsername(token);
        // return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        return (username.equals("testUser") && !isTokenExpired(token)); // Placeholder for UserDetails
    }

    // Extract username from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract expiration date from token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract specific claim from token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // For retrieving any information from token we will need the secret key
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    // Check if the token has expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Setter for properties (if not using @Value for some reason, e.g. manual instantiation)
    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setExpirationMs(long expirationMs) {
        this.expirationMs = expirationMs;
    }
}
