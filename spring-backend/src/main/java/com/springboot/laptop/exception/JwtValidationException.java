package com.springboot.laptop.exception;

public class JwtValidationException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public JwtValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
