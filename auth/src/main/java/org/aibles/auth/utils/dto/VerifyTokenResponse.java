package org.aibles.auth.utils.dto;

import lombok.Data;
@Data
public class VerifyTokenResponse {
    private boolean valid;
    private String message;
    private String userId;

    public VerifyTokenResponse(boolean valid, String message, String userId) {
        this.valid = valid;
        this.message = message;
        this.userId = userId;
    }

}


