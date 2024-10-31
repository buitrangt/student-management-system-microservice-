package org.aibles.auth.utils.exception;


import org.springframework.http.HttpStatus;

public class InvalidTokenException extends ApplicationBaseRuntimeException {
    public InvalidTokenException(String reason) {
        setStatus(HttpStatus.UNAUTHORIZED.value());
        setErrorCode("org.aibles.java.onlineshopping.utils.exception.InvalidTokenException");
        addParam(reason);
    }
}


