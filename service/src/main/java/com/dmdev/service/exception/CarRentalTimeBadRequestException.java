package com.dmdev.service.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CarRentalTimeBadRequestException extends ResponseStatusException {

    public CarRentalTimeBadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}