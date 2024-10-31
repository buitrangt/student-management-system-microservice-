package org.aibles.auth.utils.controller.converter;


import org.aibles.auth.utils.dto.LoginRequest;
import org.aibles.auth.utils.dto.LoginResponse;
import org.aibles.auth.utils.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class LoginDtoConverter {

    public static LoginResponse toLoginResponse(String accessToken, String refreshToken, Long accessTokenExpiry, Long refreshTokenExpiry) {
        return new LoginResponse(accessToken, refreshToken, accessTokenExpiry, refreshTokenExpiry);
    }

    public static Account toEntity(LoginRequest loginRequest) {
        Account account = new Account();
        account.setUsername(loginRequest.getUsername());
        account.setPassword(loginRequest.getPassword());
        return account;
    }
}
