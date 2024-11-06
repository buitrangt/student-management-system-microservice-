package org.aibles.auth.utils.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.aibles.auth.utils.constants.CommonConstants;
import org.aibles.auth.utils.entity.Account;
import org.aibles.auth.utils.exception.BadRequestException;
import org.aibles.auth.utils.exception.PasswordNotMatchException;
import org.aibles.auth.utils.exception.ResourceNotFoundException;
import org.aibles.auth.utils.helper.RedisService;
import org.aibles.auth.utils.repository.AccountRepository;
import org.aibles.auth.utils.service.JWTService;
import org.aibles.auth.utils.service.LoginService;
import org.aibles.auth.utils.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class LoginServiceImpl implements LoginService {

    private final AccountRepository accountRepository;
    private final RedisService redisService;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Value("${expirationMs:3600000}")
    private Long jwtExpirationMs;

    @Value("${refreshExpirationMs:86400000}")
    private Long jwtRefreshExpirationMs;

    public LoginServiceImpl(AccountRepository accountRepository, RedisService redisService, JWTService jwtService,
                            PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.accountRepository = accountRepository;
        this.redisService = redisService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        log.info("Login request for username: {}", username);

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("username: ", username));

        Long unlockTime = redisService.getOrDefault(CommonConstants.UNLOCK_TIME_KEY + username, username, null);
        if (unlockTime != null && Instant.now().getEpochSecond() < unlockTime) {
            log.info("Account is temporarily locked: {}", username);
            throw new BadRequestException("Account is temporarily locked");
        }

        if (!passwordEncoder.matches(password, account.getPassword())) {
            log.info("Password does not match for username: {}", username);
            incrementFailedLoginAttempts(username);
            throw new PasswordNotMatchException("Password does not match");
        }

        redisService.delete(CommonConstants.FAILED_PASSWORD_ATTEMPT_KEY, username);

        String accessToken = jwtService.generateAccessToken(account);
        String refreshToken = jwtService.generateRefreshToken(account);

        String userId = account.getUserId();
        long expiresAt = Instant.now().getEpochSecond() + jwtExpirationMs;
        tokenService.saveToken(userId, accessToken, expiresAt);

        redisService.saveWithExpire(username, CommonConstants.ACCESS_TOKEN_HASH_KEY, accessToken, jwtExpirationMs);
        redisService.saveWithExpire(username, CommonConstants.REFRESH_TOKEN_HASH_KEY, refreshToken, jwtRefreshExpirationMs);

        log.info("Login successful for username: {}", username);

        Map<String, Object> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        tokens.put("expiresIn", jwtExpirationMs);
        tokens.put("refreshExpiresIn", jwtRefreshExpirationMs);

        return tokens;
    }

    private void incrementFailedLoginAttempts(String username) {
        Integer attempts = redisService.getOrDefault(CommonConstants.FAILED_PASSWORD_ATTEMPT_KEY, username, 0);
        log.info("Current failed login attempts for {}: {}", username, attempts);

        attempts += 1;
        redisService.save(CommonConstants.FAILED_PASSWORD_ATTEMPT_KEY, username, attempts);

        if (attempts == 5) {
            lockTemporarily(username, CommonConstants.FIVE_MINUTES, "5");
        } else if (attempts == 10) {
            lockTemporarily(username, CommonConstants.TEN_MINUTES, "10");
        } else if (attempts >= 15) {
            lockAccountPermanently(username);
        }
    }

    private void lockTemporarily(String username, long duration, String attemptCount) {
        long lockTime = Instant.now().getEpochSecond() + duration;
        redisService.saveWithExpire(
                CommonConstants.UNLOCK_TIME_KEY + username,
                username,
                String.valueOf(lockTime),
                duration
        );
        log.info("Account temporarily locked due to {} failed login attempts: {}", attemptCount, username);
        throw new BadRequestException("Account temporarily locked due to " + attemptCount + " failed login attempts");
    }

    private void lockAccountPermanently(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("username: ", username));

        redisService.delete(CommonConstants.UNLOCK_TIME_KEY + username, username);
        account.setLockPermanent(true);
        accountRepository.save(account);

        tokenService.revokeAllTokensForUser(account.getUserId());

        log.info("Account permanently locked due to 15 failed login attempts: {}", username);
        throw new BadRequestException("Account permanently locked due to 15 failed login attempts");
    }

    @Override
    public void logout(String accessToken) {
        log.info("Logout request for token: {}", accessToken);

        if (!tokenService.isTokenValid(accessToken)) {
            throw new BadRequestException("Token không hợp lệ hoặc đã hết hạn");
        }

        tokenService.revokeToken(accessToken);

        String username = jwtService.getUsernameFromToken(accessToken);
        redisService.delete(CommonConstants.ACCESS_TOKEN_HASH_KEY, username);

        log.info("Logout successful for token: {}", accessToken);
    }


}
