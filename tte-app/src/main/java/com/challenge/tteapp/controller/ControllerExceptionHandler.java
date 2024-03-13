package com.challenge.tteapp.controller;

import com.challenge.tteapp.processor.ValidationError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.ParseException;
@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private final ValidationError validationError;
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> exceptionClass(Exception e) {
        log.error(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(validationError.getStructureError(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR),
                e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public final ResponseEntity<Object> httpServerExcepcion(HttpServerErrorException e) {
        log.error(e.getMessage(),e.getStatusCode().value());
        return new ResponseEntity<>(validationError.getStructureError(String.valueOf(e.getStatusCode().value()),
                e.getMessage()), HttpStatus.OK);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public final ResponseEntity<Object> httpClientExcepcion(HttpClientErrorException e) {
        String errorMessage = e.getStatusText();
        int statusCode = e.getStatusCode().value();
        log.error(e.getMessage());
        return new ResponseEntity<>(validationError.getStructureError(errorMessage, String.valueOf(statusCode)),
                e.getStatusCode());    }
    @ExceptionHandler(ParseException.class)
    public final ResponseEntity<Object> parseExcepcion(ParseException e) {
        log.error(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(validationError.getStructureError(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
