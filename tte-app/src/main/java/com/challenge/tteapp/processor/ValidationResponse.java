package com.challenge.tteapp.processor;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ValidationResponse {

    private final ValidationError validationError;
    public ResponseEntity<Object> createDuplicateResponse(String field, String requestId) {
        log.warn("{} duplicated, requestId: {}", field, requestId);
        return new ResponseEntity<>(validationError.getStructureError(HttpStatus.BAD_REQUEST.value(),
                field + " exist in database"), HttpStatus.BAD_REQUEST);
    }


}
