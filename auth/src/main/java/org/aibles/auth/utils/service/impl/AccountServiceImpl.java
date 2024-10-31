package org.aibles.auth.utils.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.aibles.auth.utils.entity.Account;
import org.aibles.auth.utils.exception.PasswordNotMatchException;
import org.aibles.auth.utils.repository.AccountRepository;
import org.aibles.auth.utils.service.AccountService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void createAccount(String userId, String username, String password, String confirmPassword) {
        log.info("Creating account for {}", userId);
        if (!password.equals(confirmPassword)) {
            log.error("Passwords don't match");
            throw new PasswordNotMatchException("Passwords don't match");
        }
        String encoderPass = passwordEncoder.encode(password);

        Account account = new Account();
        account.setUserId(userId);
        account.setUsername(username);
        account.setPassword(encoderPass);
        account.setCreatedAt(System.currentTimeMillis());
        account.setCreatedBy(username);
        accountRepository.save(account);
        log.info("Account created successfully");
    }
}
