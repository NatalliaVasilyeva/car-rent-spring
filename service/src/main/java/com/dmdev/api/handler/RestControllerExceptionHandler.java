package com.dmdev.api.handler;

import com.dmdev.service.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.rmi.ServerException;
import java.time.LocalDateTime;
import java.util.Collections;

import static java.util.stream.Collectors.*;

@Slf4j
@RestControllerAdvice(basePackages = "com.dmdev.api.rest")
public class RestControllerExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public RestErrorResponse handleNotFoundException(NotFoundException ex) {
        return RestErrorResponse.builder()
                .messages(Collections.singletonList(ex.getMessage()))
                .status(HttpStatus.NOT_FOUND)
                .time(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestErrorResponse handleValidationErrors(MethodArgumentNotValidException ex) {
        return RestErrorResponse.builder()
                .messages(ex.getBindingResult().getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(toList()))
                .status(HttpStatus.BAD_REQUEST)
                .time(LocalDateTime.now())
                .build();
    }


    @ExceptionHandler(ServerException.class)
    public RestErrorResponse handleServerInternalException(ServerException ex) {
        return RestErrorResponse.builder()
                .messages(Collections.singletonList(ex.getMessage()))
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .time(LocalDateTime.now())
                .build();
    }
}