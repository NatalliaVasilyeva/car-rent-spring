package com.dmdev.service.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserBadRequestException extends ResponseStatusException {

    public UserBadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}