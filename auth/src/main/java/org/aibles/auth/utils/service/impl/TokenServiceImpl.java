package org.aibles.auth.utils.service.impl;

import org.aibles.auth.utils.entity.Token;
import org.aibles.auth.utils.exception.InvalidTokenException;
import org.aibles.auth.utils.repository.TokenRepository;
import org.aibles.auth.utils.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;


@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    @Autowired
    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void saveToken(String userId, String jwtToken, long expiresAt) {
        Token token = new Token();
        token.setId(UUID.randomUUID().toString());
        token.setUserId(userId);
        token.setToken(jwtToken);
        token.setStatus("ACTIVE");
        token.setCreatedAt(System.currentTimeMillis());
        token.setExpiresAt(expiresAt);
        tokenRepository.save(token);
    }

    @Override
    public void revokeToken(String token) {
        Token tokenRecord = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Token is invalid"));
        tokenRecord.setStatus("REVOKED");
        tokenRepository.save(tokenRecord);
    }

    @Override
    public void revokeAllTokensForUser(String userId) {
        List<Token> userTokens = tokenRepository.findAllByUserIdAndStatus(userId, "ACTIVE");
        userTokens.forEach(token -> token.setStatus("REVOKED"));
        tokenRepository.saveAll(userTokens);
    }

    @Override
    public boolean isTokenValid(String token) {
        Token tokenRecord = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Token is invalid"));
        return tokenRecord.getStatus().equals("ACTIVE") && tokenRecord.getExpiresAt() > System.currentTimeMillis();
    }
}
