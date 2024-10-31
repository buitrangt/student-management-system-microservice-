package org.aibles.auth.utils.exception;


import org.springframework.http.HttpStatus;

public class BadRequestException extends ApplicationBaseRuntimeException {

    public BadRequestException(String message) {
        setStatus(HttpStatus.BAD_REQUEST.value());
        setErrorCode("org.aibles.auth.utils.exception.BadRequestException");
        setMessage(message);
    }

    public BadRequestException(String message, Object... params) {
        setStatus(HttpStatus.BAD_REQUEST.value());
        setErrorCode("org.aibles.auth.utils.exception.BadRequestException");
        setMessage(message);
    }
}

