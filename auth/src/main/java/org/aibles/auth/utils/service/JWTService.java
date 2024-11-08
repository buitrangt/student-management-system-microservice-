package org.aibles.auth.utils.service;

import org.aibles.auth.utils.dto.VerifyTokenRequest;
import org.aibles.auth.utils.dto.VerifyTokenResponse;
import org.aibles.auth.utils.entity.Account;

public interface JWTService {
    String generateAccessToken(Account account);
    String generateRefreshToken(Account account);
    VerifyTokenResponse verifyToken(VerifyTokenRequest request);
    String getUsernameFromToken(String token);
    boolean isTokenExpired(String token);
}
