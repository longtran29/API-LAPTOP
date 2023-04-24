package com.springboot.laptop.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class MyServiceAdvice {


    /*
        handle exception
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> noSuchElement() {
        return new ResponseEntity<String>("Không tìm thấy, vui lòng kiểm tra lại ",HttpStatus.NOT_FOUND);
    }

}
