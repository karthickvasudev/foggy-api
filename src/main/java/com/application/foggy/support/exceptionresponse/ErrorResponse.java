package com.application.foggy.support.exceptionresponse;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponse extends RuntimeException {
    private HttpStatus statusCode;
    private String errorMsg;

    public ErrorResponse(HttpStatus statusCode, String errorMsg) {
        this.statusCode = statusCode;
        this.errorMsg = errorMsg;
    }


}
