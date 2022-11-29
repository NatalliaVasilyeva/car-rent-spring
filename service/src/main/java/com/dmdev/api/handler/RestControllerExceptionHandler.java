package com.dmdev.api.handler;

import com.dmdev.service.exception.NotFoundException;
import com.dmdev.service.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.rmi.ServerException;
import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@RestControllerAdvice(basePackages = "com.dmdev.api.rest")
public class RestControllerExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<RestErrorResponse> handleNotFoundException(NotFoundException ex) {
        return createResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<RestErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        return createResponse(ex, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<RestErrorResponse> handleUnauthorizedException(HttpClientErrorException.Forbidden ex) {
        return createResponse(ex, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        return createResponse(ex, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ServerException.class)
    public ResponseEntity<RestErrorResponse> handleServerInternalException(ServerException ex) {
        return createResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<RestErrorResponse> createResponse(Exception ex, HttpStatus status) {
        return new ResponseEntity<>(RestErrorResponse.builder()
                .messages(Collections.singletonList(ex.getMessage()))
                .status(status)
                .time(LocalDateTime.now())
                .build(), status);
    }
}