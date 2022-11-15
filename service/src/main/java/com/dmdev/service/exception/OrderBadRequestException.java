package com.dmdev.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OrderBadRequestException extends ResponseStatusException {

    public OrderBadRequestException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}