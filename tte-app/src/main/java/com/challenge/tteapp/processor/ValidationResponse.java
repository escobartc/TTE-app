package com.challenge.tteapp.processor;


import com.challenge.tteapp.model.UserResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@RequiredArgsConstructor
@Component
public class ValidationResponse {

    private final ValidationError validationError;
    public ResponseEntity<UserResponse> createDuplicateResponse(String field, String requestId) {
        log.warn("{} duplicated, with requestId: [{}]", field, requestId);
        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, field + " exist in database");
    }

}
