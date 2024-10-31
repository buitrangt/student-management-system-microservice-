package org.aibles.student.controller.advice;

import lombok.extern.slf4j.Slf4j;

import org.aibles.student.dto.BaseResponse;
import org.aibles.student.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BaseResponse<Void>> handleBusinessException(BusinessException businessException) {
        return new ResponseEntity<>(
                BaseResponse.fail(businessException.getResponseStatus()),
                businessException.getResponseStatus().getStatus()
        );
    }


}

