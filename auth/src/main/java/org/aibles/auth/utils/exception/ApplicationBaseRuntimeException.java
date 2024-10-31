package org.aibles.auth.utils.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class ApplicationBaseRuntimeException extends RuntimeException {
    private int status;
    private String errorCode;
    private String message;
    private List<String> params;

    protected void addParam(String element) {
        if (Objects.isNull(this.params)) {
            this.params = new ArrayList<>();
        }
        this.params.add(element);
    }
}
