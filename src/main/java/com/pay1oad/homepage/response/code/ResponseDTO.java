package com.pay1oad.homepage.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ResponseDTO {

    private HttpStatus httpStatus;
    private final String code;
    private final String message;
}
