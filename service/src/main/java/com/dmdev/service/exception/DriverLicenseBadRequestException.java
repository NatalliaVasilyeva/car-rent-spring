package com.dmdev.service.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DriverLicenseBadRequestException extends ResponseStatusException {

    public DriverLicenseBadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}