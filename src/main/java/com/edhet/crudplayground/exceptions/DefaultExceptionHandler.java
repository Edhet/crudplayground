package com.edhet.crudplayground.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultExceptionHandler {
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleException(MissingServletRequestParameterException ex) {
        String errorMsg = "Missing " + ex.getParameterName() + " in request";
        return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);
    }
}