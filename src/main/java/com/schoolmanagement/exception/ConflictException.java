package com.schoolmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) //Bizim belirlediğimiz status code ile exception fırlasın.
public class ConflictException extends RuntimeException{

    public ConflictException(String message) {
        super(message);
    }
}
