package com.pay1oad.homepage.exception;

import com.pay1oad.homepage.exception.CustomException;
import com.pay1oad.homepage.response.ResForm;
import com.pay1oad.homepage.response.code.status.ErrorStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResForm<Void>> handleCustomException(CustomException ex) {
        ErrorStatus errorStatus = ex.getErrorStatus();
        ResForm<Void> response = ResForm.error(
                errorStatus.getCode(),
                errorStatus.getMessage(),
                null
        );
        return new ResponseEntity<>(response, errorStatus.getHttpStatus());
    }

}
