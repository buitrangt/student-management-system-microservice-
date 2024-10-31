package org.aibles.auth.utils.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApplicationBaseRuntimeException {
    public ResourceNotFoundException(String resourceName, String id) {
        setStatus(HttpStatus.NOT_FOUND.value());
        setErrorCode("org.aibles.auth.utils.exception.ResourceNotFoundException");
        addParam(resourceName);
        addParam(id);
    }
}
