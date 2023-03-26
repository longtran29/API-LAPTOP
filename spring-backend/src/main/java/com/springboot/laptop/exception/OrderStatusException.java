package com.springboot.laptop.exception;

public class OrderStatusException extends RuntimeException {

    private final String message;

    public OrderStatusException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
