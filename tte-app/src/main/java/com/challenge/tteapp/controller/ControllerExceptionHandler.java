package com.challenge.tteapp.controller;

import com.challenge.tteapp.processor.ValidationError;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    private final ValidationError validationError;

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> exceptionClass(Exception e) {
        log.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(validationError.getStructureError(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR),
                e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> authenticationException(AuthenticationException e) {
        log.error(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(validationError.getStructureError(String.valueOf(HttpStatus.UNAUTHORIZED),
                e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });
        String errorMessage = !errors.isEmpty() ? errors.get(0) : "Unknown error";
        int statusCode = HttpStatus.BAD_REQUEST.value();
        log.error(errorMessage, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(validationError.getStructureError(errorMessage, String.valueOf(statusCode)));
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public final ResponseEntity<Object> httpServerException(HttpServerErrorException e) {
        log.error(e.getMessage(), e.getStatusCode().value());
        return new ResponseEntity<>(validationError.getStructureError(String.valueOf(e.getStatusCode().value()),
                e.getMessage()), HttpStatus.OK);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public final ResponseEntity<Object> httpClientException(HttpClientErrorException e) {
        String errorMessage = e.getStatusText();
        int statusCode = e.getStatusCode().value();
        log.error(e.getMessage());
        return new ResponseEntity<>(validationError.getStructureError(errorMessage, String.valueOf(statusCode)),
                e.getStatusCode());
    }

    @ExceptionHandler(ParseException.class)
    public final ResponseEntity<Object> parseException(ParseException e) {
        log.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(validationError.getStructureError(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
