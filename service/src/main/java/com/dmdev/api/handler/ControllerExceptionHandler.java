package com.dmdev.api.handler;

import com.dmdev.service.exception.BrandBadRequestException;
import com.dmdev.service.exception.CategoryBadRequestException;
import com.dmdev.service.exception.DriverLicenseBadRequestException;
import com.dmdev.service.exception.NotFoundException;
import com.dmdev.service.exception.OrderBadRequestException;
import com.dmdev.service.exception.UnauthorizedException;
import com.dmdev.service.exception.UserBadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.rmi.ServerException;
import java.time.LocalDate;

@Slf4j
@ControllerAdvice(basePackages = "com.dmdev.api.controller")
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(Exception ex, HttpServletRequest request, Model model) {
        log.error("Request: " + request.getRequestURL() + " raised " + ex);
        addAttributes(model, request, ex);

        return "/error/error404";
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public String handleUnauthorizedException(Exception ex, HttpServletRequest request, Model model) {
        log.error("Request: " + request.getRequestURL() + " raised " + ex);
        addAttributes(model, request, ex);

        return "/error/error401";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleNotValidException(Exception ex, HttpServletRequest request, Model model) {
        log.error("Request: " + request.getRequestURL() + " raised " + ex);
        addAttributes(model, request, ex);

        return "/error/error400";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BrandBadRequestException.class, DriverLicenseBadRequestException.class, OrderBadRequestException.class,
            CategoryBadRequestException.class, UserBadRequestException.class})
    public String handleBadRequestException(Exception ex, HttpServletRequest request, Model model) {
        log.error("Request: " + request.getRequestURL() + " raised " + ex);
        addAttributes(model, request, ex);

        return "/error/error400";
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServerException.class)
    public String handleServerInternalException(Exception ex, HttpServletRequest request, Model model) {
        log.error("Request: " + request.getRequestURL() + " raised " + ex);
        addAttributes(model, request, ex);

        return "/error/error500";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    private void addAttributes(Model model, HttpServletRequest request, Exception ex) {
        model.addAttribute("timestamp", LocalDate.now());
        model.addAttribute("url", request.getRequestURL());
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("error", ex.getCause());
    }
}