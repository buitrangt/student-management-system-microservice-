package org.aibles.auth.utils.exception;

import org.springframework.http.HttpStatus;

public class PasswordNotMatchException extends ApplicationBaseRuntimeException {
    public PasswordNotMatchException(String message) {
        setStatus(HttpStatus.CONFLICT.value());
        setErrorCode("org.aibles.java.online_shopping.features.users.exception.PasswordNotMatchException");
        setMessage(message);
    }
}
