package org.aibles.auth.utils.controller.converter;

import org.aibles.auth.utils.dto.RegisterUserRequest;
import org.aibles.auth.utils.entity.Account;
import org.aibles.auth.utils.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AccountDtoConverter {
    public static Account toEntity(RegisterUserRequest registerUserRequest, User user) {
        Account accountEntity = new Account();
        accountEntity.setAccountId(user.getUserId());
        accountEntity.setUsername(registerUserRequest.getUsername());
        accountEntity.setPassword(registerUserRequest.getPassword());

        return accountEntity;
    }
}
