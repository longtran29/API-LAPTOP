package com.springboot.laptop.advice;

import com.springboot.laptop.exception.ApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class MyServiceAdvice {

    /**
     *
     * ControllerAdvice only handles for Controller, not handle for Service or Repository
      */

    private final String DETAIL = "Detail: ";
    /*
         global exceptional handling
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> noSuchElement() {
        return new ResponseEntity<String>("Không tìm thấy, vui lòng kiểm tra lại ",HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex, ServletWebRequest request) {

        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        List<String> errors = constraintViolations
                .stream()
                .map(err -> err.getRootBeanClass().getName() + " " +
                        err.getPropertyPath() + ": " + err.getMessage())
                .collect(Collectors.toList());

        ApiError apiError = new ApiError();
        apiError.setErrors(errors);
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        apiError.setPath(request.getRequest().getRequestURI());
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }


}
