package com.edhet.crudplayground.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StudentNotFoundException extends IllegalArgumentException {
    public StudentNotFoundException(String error) {
        super(error);
    }
}
