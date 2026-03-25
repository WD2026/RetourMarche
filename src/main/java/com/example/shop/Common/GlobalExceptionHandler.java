package com.example.shop.Common;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        log.error("An unexpected error occurred: ", e);
        model.addAttribute("error", e.getMessage());
        return "error/500";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle404(Model model) {
        return "error/404";
    }
}
