package com.dmdev.api.handler;

import com.dmdev.service.exception.NotFoundException;
import com.dmdev.service.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.rmi.ServerException;
import java.time.LocalDateTime;
import java.util.Collections;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestControllerAdvice(basePackages = "com.dmdev.api.rest")
public class RestControllerExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<RestErrorResponse> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(RestErrorResponse.builder()
                .messages(Collections.singletonList(ex.getMessage()))
                .status(HttpStatus.NOT_FOUND)
                .time(LocalDateTime.now())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<RestErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        return new ResponseEntity<>(RestErrorResponse.builder()
                .messages(Collections.singletonList(ex.getMessage()))
                .status(HttpStatus.UNAUTHORIZED)
                .time(LocalDateTime.now())
                .build(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<RestErrorResponse> handleUnauthorizedException(HttpClientErrorException.Forbidden ex) {
        return new ResponseEntity<>(RestErrorResponse.builder()
                .messages(Collections.singletonList(ex.getMessage()))
                .status(HttpStatus.FORBIDDEN)
                .time(LocalDateTime.now())
                .build(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(RestErrorResponse.builder()
                .messages(ex.getBindingResult().getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(toList()))
                .status(HttpStatus.BAD_REQUEST)
                .time(LocalDateTime.now())
                .build(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ServerException.class)
    public ResponseEntity<RestErrorResponse> handleServerInternalException(ServerException ex) {
        return new ResponseEntity<>(RestErrorResponse.builder()
                .messages(Collections.singletonList(ex.getMessage()))
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .time(LocalDateTime.now())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}