package com.edhet.crudplayground.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDatabaseSelectedException extends IllegalArgumentException {
    public InvalidDatabaseSelectedException(String error) {
        super(error);
    }
}
