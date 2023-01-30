package com.springboot.laptop.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class DeleteDataFail extends Exception{

    public DeleteDataFail(String message) {
        super(message);
    }
}
