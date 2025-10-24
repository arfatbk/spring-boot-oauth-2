package com.example.resource.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ErrorAdvise {


    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse handleNotFoundException(NotFoundException ex) {
        return ErrorResponse
                .builder(ex, HttpStatus.NOT_FOUND, ex.getMessage())
                .build();
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ErrorResponse handleNoResourceFoundException(NoResourceFoundException ex) {
        return ErrorResponse
                .builder(ex, HttpStatus.NOT_FOUND, ex.getResourcePath())
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGenericException(Exception ex) {
        return ErrorResponse
                .builder(ex, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred")
                .build();
    }
}
