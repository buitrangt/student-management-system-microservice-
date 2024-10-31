package org.aibles.auth.utils.service;

import org.aibles.auth.utils.dto.LoginRequest;

public interface AccountService {
    void createAccount(String userId, String username, String password, String confirmPassword);

}
