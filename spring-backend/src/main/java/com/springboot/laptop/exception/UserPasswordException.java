package com.springboot.laptop.exception;

public class UserPasswordException extends RuntimeException{

    public UserPasswordException(String message) {
        super(message);
    }
}
