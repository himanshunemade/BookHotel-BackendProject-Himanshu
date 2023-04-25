package com.sweethome.bookingservice.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ErrorMessage resourceNotFoundException(Exception ex) {
        ErrorMessage message = new ErrorMessage(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value());

        return message;
    }
}
