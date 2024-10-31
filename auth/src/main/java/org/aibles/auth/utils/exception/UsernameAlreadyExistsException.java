package org.aibles.auth.utils.exception;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistsException extends ApplicationBaseRuntimeException {
    public UsernameAlreadyExistsException(String message, String username) {
        setStatus(HttpStatus.CONFLICT.value());
        setErrorCode("org.aibles.java.online_shopping.features.users.exception.UserNameInValidException");
        setMessage(message);
        addParam(username);
    }
}
