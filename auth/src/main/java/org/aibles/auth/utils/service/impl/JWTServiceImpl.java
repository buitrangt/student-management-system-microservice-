package org.aibles.auth.utils.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.aibles.auth.utils.dto.VerifyTokenRequest;
import org.aibles.auth.utils.dto.VerifyTokenResponse;
import org.aibles.auth.utils.entity.Account;
import org.aibles.auth.utils.exception.InvalidTokenException;
import org.aibles.auth.utils.service.JWTService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
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
                .setSubject(account.getUserId()) // Đặt `accountId` (hoặc `userId`) vào `subject`
                .claim("username", account.getUsername()) // Lưu `username` vào một claim riêng nếu cần
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    @Override
    public String generateRefreshToken(Account account) {
        return Jwts.builder()
                .setSubject(account.getUserId()) // Đặt `accountId` (hoặc `userId`) vào `subject`
                .claim("username", account.getUsername()) // Lưu `username` vào một claim riêng nếu cần
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
            String userId = claims.getSubject(); // Giả sử userId được lưu trong `subject`

            return new VerifyTokenResponse(isValid, isValid ? "Token is valid" : "Token has expired", userId);
        } catch (JwtException | IllegalArgumentException e) {
            return new VerifyTokenResponse(false, "Invalid token", null);
        }
    }


}
