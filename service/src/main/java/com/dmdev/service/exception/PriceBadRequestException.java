package com.dmdev.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PriceBadRequestException extends ResponseStatusException {

    public PriceBadRequestException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}