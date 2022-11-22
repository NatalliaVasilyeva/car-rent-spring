package com.dmdev.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CategoryBadRequestException extends ResponseStatusException {

    public CategoryBadRequestException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}