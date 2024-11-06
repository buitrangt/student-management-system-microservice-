package org.aibles.auth.utils.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.aibles.auth.utils.dto.VerifyTokenRequest;
import org.aibles.auth.utils.dto.VerifyTokenResponse;
import org.aibles.auth.utils.entity.Account;
import org.aibles.auth.utils.exception.InvalidTokenException;
import org.aibles.auth.utils.service.JWTService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class JWTServiceImpl implements JWTService {
    @Value("${Secret}")
    private String jwtSecret;
    @Value("${expirationMs}")
    private Long jwtExpirationMs;
    @Value("${refreshExpirationMs}")
    private Long jwtRefreshExpirationMs;

    @Override
    public String generateAccessToken(Account account) {
        return Jwts.builder()
                .setSubject(account.getUserId())
                .claim("username", account.getUsername())
                .claim("role", account.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    @Override
    public String generateRefreshToken(Account account) {
        return Jwts.builder()
                .setSubject(account.getUserId())
                .claim("username", account.getUsername())
                .claim("role", account.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    @Override
    public VerifyTokenResponse verifyToken(VerifyTokenRequest request) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(request.getToken())
                    .getBody();

            boolean isValid = !claims.getExpiration().before(new Date());
            return new VerifyTokenResponse(isValid, isValid ? "Token is valid" : "Token has expired");
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Token verification failed: {}", e.getMessage());
            return new VerifyTokenResponse(false, "Invalid token");
        }
    }

    @Override
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("username", String.class);
    }



}
