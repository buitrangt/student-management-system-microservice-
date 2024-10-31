package org.aibles.auth.utils.exception;

import org.springframework.http.HttpStatus;

public class ApplicationGeneralException extends ApplicationBaseRuntimeException {
    public ApplicationGeneralException(String message) {
        setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        setErrorCode("org.aibles.java.online_shopping.utils.exception.ApplicationGeneralException");
        setMessage(message);
    }
}
