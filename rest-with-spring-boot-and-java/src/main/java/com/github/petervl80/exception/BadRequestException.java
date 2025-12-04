package com.github.petervl80.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public BadRequestException() {
        super("Unsupported operation!");
    }

    public BadRequestException(String exception) {
        super(exception);
    }
    
}
