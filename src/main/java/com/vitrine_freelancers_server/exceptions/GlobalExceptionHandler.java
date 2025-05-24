package com.vitrine_freelancers_server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        return e.getMessage();
    }

    @ExceptionHandler(NullPointerException.class)
    public String handleNullPointerException(NullPointerException e) {
        return e.getMessage();
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException e) {
        return e.getMessage();
    }

    @ExceptionHandler(UserNotAuthorizationException.class)
    public ResponseEntity<ResponseError> handleUserNotAuthorization(UserNotAuthorizationException e) {
        ResponseError responseError = new ResponseError(
                e.getMessage(),
                "user not authorized",
                HttpStatus.FORBIDDEN.value()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseError);
    }

    @ExceptionHandler(JobNotFoundException.class)
    public ResponseEntity<ResponseError> handleJobNotFound(JobNotFoundException e) {
        ResponseError responseError = new ResponseError(
                e.getMessage(),
                "job not found",
                HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
    }
}
