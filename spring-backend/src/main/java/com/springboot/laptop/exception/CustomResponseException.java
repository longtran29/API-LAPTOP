package com.springboot.laptop.exception;

import com.springboot.laptop.model.dto.response.StatusResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CustomResponseException extends ResponseStatusException {


    public CustomResponseException(StatusResponseDTO response) {
        super(HttpStatus.valueOf(Integer.parseInt(response.getCode())), response.getMessage());
        System.out.println("Value in custom exception " + response.getCode() + " message " + response.getMessage());
    }

}
