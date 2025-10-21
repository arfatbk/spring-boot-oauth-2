package com.example.auth.security;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Arfat A. Chaus
 * since 2025-10-23
 */

@ControllerAdvice
class ExceptionHandlingAdvisor {


    @ExceptionHandler(Exception.class)
    @ResponseBody
    String handleException(Exception e) {
        return "An error occurred: " + e.getMessage();
    }
}
