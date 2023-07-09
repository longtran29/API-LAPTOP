package com.springboot.laptop.advice;

import com.springboot.laptop.model.dto.response.OperationResponse;
import com.springboot.laptop.model.dto.response.StatusResponseDTO;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class MyServiceAdvice {

    private final String DETAIL = "Detail: ";
    /*
         global exceptional handling
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> noSuchElement() {
        return new ResponseEntity<String>("Không tìm thấy, vui lòng kiểm tra lại ",HttpStatus.NOT_FOUND);
    }
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<?> handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
//        String cause = e.getCause().getCause().getMessage();
//        cause = cause.substring(cause.indexOf(DETAIL) + DETAIL.length());
//        OperationResponse resp = new OperationResponse();
//        resp.setOperationStatus(StatusResponseDTO.DATA_CONFLICT);
//        resp.setOperationMessage(cause);
//        return ResponseEntity.ok(resp);
//    }

}
