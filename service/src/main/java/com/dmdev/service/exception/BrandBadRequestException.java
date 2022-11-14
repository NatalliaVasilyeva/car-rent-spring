package com.dmdev.service.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BrandBadRequestException extends ResponseStatusException {

    public BrandBadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}