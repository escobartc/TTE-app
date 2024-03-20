package com.challenge.tteapp;

import com.challenge.tteapp.controller.ControllerExceptionHandler;
import com.challenge.tteapp.processor.ValidationError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ControllerExceptionHandlerTest {

    @Mock
    private ValidationError validationError;

    @InjectMocks
    private ControllerExceptionHandler controllerExceptionHandler;

    @Test
    void testExceptionClass() {
        Exception exception = new Exception("Test exception message");
        ResponseEntity<Object> responseEntity = controllerExceptionHandler.exceptionClass(exception);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        verify(validationError).getStructureError(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR), exception.getMessage());
    }

    @Test
    void testAuthenticationException() {
        AuthenticationException exception = new AuthenticationException("Test exception message") {
        };
        ResponseEntity<Object> responseEntity = controllerExceptionHandler.authenticationException(exception);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        verify(validationError).getStructureError(String.valueOf(HttpStatus.UNAUTHORIZED), exception.getMessage());
    }

    @Test
    void testHttpServerException() {
        HttpServerErrorException exception = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        ResponseEntity<Object> responseEntity = controllerExceptionHandler.httpServerExcepcion(exception);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(validationError).getStructureError(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), exception.getMessage());
    }

    @Test
    void testHttpClientException() {
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad request");
        ResponseEntity<Object> responseEntity = controllerExceptionHandler.httpClientExcepcion(exception);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(validationError).getStructureError("Bad request", String.valueOf(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void testParseException() {
        ParseException exception = new ParseException("Parse error", 0);
        ResponseEntity<Object> responseEntity = controllerExceptionHandler.parseExcepcion(exception);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        verify(validationError).getStructureError(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), exception.getMessage());
    }
}

