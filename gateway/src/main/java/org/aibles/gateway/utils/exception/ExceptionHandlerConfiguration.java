package org.aibles.gateway.utils.exception;

import lombok.RequiredArgsConstructor;
import org.aibles.gateway.utils.dto.ApplicationResponse;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerConfiguration {

    private final MessageSource messageSource;

    @ExceptionHandler({ApplicationBaseRuntimeException.class})
    public ResponseEntity<ApplicationResponse<Void>> handleApplicationBaseRuntimeException(
            ApplicationBaseRuntimeException exception, Locale locale
    ) {
        var message = messageSource.getMessage(exception.getErrorCode(), exception.getParams().toArray(), locale);
        var errorResponse = ApplicationResponse.error(exception.getStatus(), message);
        return ResponseEntity
                .status(HttpStatus.valueOf(exception.getStatus()))
                .body(errorResponse);
    }
}