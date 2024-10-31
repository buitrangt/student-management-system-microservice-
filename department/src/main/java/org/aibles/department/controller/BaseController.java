package org.aibles.department.controller;


import org.aibles.department.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {

    protected <T> ResponseEntity<BaseResponse<T>> successResponse(T data, HttpStatus status) {
        return new ResponseEntity<>(BaseResponse.success(data), status);
    }

    protected <T> ResponseEntity<BaseResponse<T>> successResponse(T data) {
        return successResponse(data, HttpStatus.OK);
    }

    protected <T> ResponseEntity<BaseResponse<Void>> successResponseNoContent() {
        return new ResponseEntity<>(BaseResponse.success(), HttpStatus.NO_CONTENT);
    }
}

