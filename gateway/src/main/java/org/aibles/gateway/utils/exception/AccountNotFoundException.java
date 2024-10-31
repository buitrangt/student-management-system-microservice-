package org.aibles.gateway.utils.exception;

import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends ApplicationBaseRuntimeException {
    public AccountNotFoundException(String userId) {
        setStatus(HttpStatus.NOT_FOUND.value());
        setErrorCode("org.aibles.java.online_shopping.utils.exception.AccountNotFoundException");
        addParam(userId);

    }
}
