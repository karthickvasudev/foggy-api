package com.application.foggy.support.exceptionresponse;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponses extends RuntimeException {
    private HttpStatus statusCode;
    private String[] errorMsgs;

    public ErrorResponses(HttpStatus statusCode, String... errorMsgs) {
        this.statusCode = statusCode;
        this.errorMsgs = errorMsgs;
    }
}
