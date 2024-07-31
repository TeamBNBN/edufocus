package com.edufocus.edufocus.user.model.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("Token is invalid");
    }
}