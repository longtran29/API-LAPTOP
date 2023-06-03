package com.springboot.laptop.advice;

import com.springboot.laptop.exception.JwtValidationException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@ControllerAdvice
public class MyServiceAdvice {


    /*
         global exceptional handling
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> noSuchElement() {
        return new ResponseEntity<String>("Không tìm thấy, vui lòng kiểm tra lại ",HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JwtValidationException.class)
    public ResponseEntity<String> handleJwtValidationException(JwtValidationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
    }

}
