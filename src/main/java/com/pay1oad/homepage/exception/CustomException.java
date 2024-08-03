package com.pay1oad.homepage.exception;

import com.pay1oad.homepage.response.code.status.ErrorStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;

public class CustomException extends RuntimeException {
    private final ErrorStatus errorStatus;

    public CustomException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }
}
