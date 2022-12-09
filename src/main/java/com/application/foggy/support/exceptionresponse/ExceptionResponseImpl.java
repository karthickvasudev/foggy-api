package com.application.foggy.support.exceptionresponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ExceptionResponseImpl extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ErrorResponse.class)
    public ResponseEntity<Object> handler(ErrorResponse e, WebRequest request) {
        return new ResponseEntity<>(Map.of("error", e.getErrorMsg()), e.getStatusCode());
    }

    @ExceptionHandler(ErrorResponses.class)
    public ResponseEntity<Object> handler(ErrorResponses e, WebRequest request) {
        return new ResponseEntity<>(Map.of("error", e.getErrorMsgs()), e.getStatusCode());
    }

}
