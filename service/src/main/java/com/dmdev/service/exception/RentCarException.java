package com.dmdev.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RentCarException extends ResponseStatusException {

    public RentCarException(HttpStatus status, String message) {
        super(status, message);
    }

    public RentCarException(HttpStatus status, String message, Throwable cause) {
        super(status, message, cause);
    }
}