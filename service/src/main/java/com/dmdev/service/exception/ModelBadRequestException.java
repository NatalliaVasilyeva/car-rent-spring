package com.dmdev.service.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ModelBadRequestException extends ResponseStatusException {

    public ModelBadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}