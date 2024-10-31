package org.aibles.lecturer.dto;

import lombok.Data;
import org.aibles.lecturer.exception.ResponseStatus;

@Data
public class BaseResponse<T> {
    private String code;
    private long timestamp;
    private T data;

    public BaseResponse(String responseCode, long timestamp, T data) {
        this.code = responseCode;
        this.timestamp = timestamp;
        this.data = data;
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>("SUCCESS", System.currentTimeMillis(), data);
    }

    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>("SUCCESS", System.currentTimeMillis(), null);
    }

    public static <T> BaseResponse<T> fail(ResponseStatus responseStatus) {
        return new BaseResponse<>(responseStatus.getCode(), System.currentTimeMillis(), null);
    }
}
