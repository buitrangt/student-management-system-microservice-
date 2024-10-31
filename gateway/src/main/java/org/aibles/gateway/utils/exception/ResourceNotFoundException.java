package org.aibles.gateway.utils.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApplicationBaseRuntimeException {
    public ResourceNotFoundException(String resourceName, String id) {
        setStatus(HttpStatus.NOT_FOUND.value());
        setErrorCode("org.aibles.java.online_shopping.utils.exception.ResourceNotFoundException");
        addParam(resourceName);
        addParam(id);
    }
}
