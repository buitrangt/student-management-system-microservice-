package org.aibles.auth.utils.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends ApplicationBaseRuntimeException {
    public EmailAlreadyExistsException(String message, String email) {
        setStatus(HttpStatus.CONFLICT.value());
        setErrorCode("org.aibles.java.online_shopping.features.users.exception.EmailAlreadyExistsException");
        setMessage(message);
        addParam(email);
    }
}
