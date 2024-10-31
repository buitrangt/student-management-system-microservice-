package org.aibles.gateway.utils.dto;

import lombok.Data;

@Data
public class TokenVerificationResponse {
    private int status;
    private String timestamp;
    private DataResponse data;

    @Data
    public static class DataResponse {
        private boolean valid;
        private String message;
        private String userId;
    }
}
