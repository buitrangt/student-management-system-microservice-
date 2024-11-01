package org.aibles.auth.utils.dto;
import lombok.Data;

@Data
public class VerifyTokenResponse {
    private boolean valid;
    private String message;

    public VerifyTokenResponse(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }
}
