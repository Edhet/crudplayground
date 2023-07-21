package com.edhet.crudplayground.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidBirthDateException extends IllegalArgumentException {
    public InvalidBirthDateException(String error) {
        super(error);
    }
}
