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

    @Value("${expirationMs:}")
    private Long jwtExpirationMs;
    @Value("${refreshExpirationMs:}")
    private Long jwtRefreshExpirationMs;

    public LoginServiceImpl(AccountRepository accountRepository, RedisService redisService, JWTService jwtService, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.redisService = redisService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        log.info("Login request for username: {}", username);

        // Tìm tài khoản theo tên người dùng
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("username: ", username));

        // Kiểm tra xem tài khoản có bị khóa tạm thời hay không
        Long unlockTime = redisService.getOrDefault(CommonConstants.UNLOCK_TIME_KEY + username,
                username, null);

        if (unlockTime != null && Instant.now().getEpochSecond() < unlockTime) {
            log.info("Account is temporarily locked: {}", username);
            throw new ResourceNotFoundException("username: ",username);
        }

        // Xác minh mật khẩu
        if (!passwordEncoder.matches(password, account.getPassword())) {
            log.info("Password does not match for username: {}", username);
            incrementFailedLoginAttempts(username);
            throw new ResourceNotFoundException("username: ",username);
        }

        // Xóa các lần đăng nhập thất bại trong Redis
        redisService.delete(CommonConstants.FAILED_PASSWORD_ATTEMPT_KEY, username);

        // Tạo các mã thông báo
        String accessToken = jwtService.generateAccessToken(account);
        String refreshToken = jwtService.generateRefreshToken(account);

        // Lưu trữ mã thông báo vào Redis
        redisService.saveWithExpire(username, CommonConstants.ACCESS_TOKEN_HASH_KEY, accessToken, jwtExpirationMs);
        redisService.saveWithExpire(username, CommonConstants.REFRESH_TOKEN_HASH_KEY, refreshToken, jwtRefreshExpirationMs);

        log.info("Login successful for username: {}", username);

        // Trả về các mã thông báo trong một Map
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

        // Tăng số lần thử đăng nhập thất bại
        attempts += 1;
        redisService.save(CommonConstants.FAILED_PASSWORD_ATTEMPT_KEY, username, attempts);

        // Khóa tạm thời sau 5 và 10 lần đăng nhập sai, khóa vĩnh viễn sau 15 lần
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

        // Xóa thời gian mở khóa tạm thời và đặt trạng thái khóa vĩnh viễn cho tài khoản
        redisService.delete(CommonConstants.UNLOCK_TIME_KEY + username, username);
        account.setLockPermanent(true);
        accountRepository.save(account);
        log.info("Account permanently locked due to 15 failed login attempts: {}", username);
        throw new BadRequestException("Account permanently locked due to 15 failed login attempts");
    }
}
