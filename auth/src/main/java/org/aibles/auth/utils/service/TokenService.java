package org.aibles.auth.utils.service;

import java.util.List;
import java.util.List;

public interface TokenService {
    void saveToken(String userId, String token, long expiresAt);
    void revokeToken(String token);
    void revokeAllTokensForUser(String userId);
    boolean isTokenValid(String token);
}

