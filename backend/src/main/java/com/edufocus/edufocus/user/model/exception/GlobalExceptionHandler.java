package com.edufocus.edufocus.user.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> handleUnAuthorizedException(InvalidTokenException e) {



        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ExpriedTokenException.class)
    public ResponseEntity<String> handleInvalidTokenException(ExpriedTokenException e) {

        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
