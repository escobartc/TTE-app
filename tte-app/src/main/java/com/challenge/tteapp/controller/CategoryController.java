package com.challenge.tteapp.controller;

import com.challenge.tteapp.model.Category;
import com.challenge.tteapp.model.dto.CategoryDTO;
import com.challenge.tteapp.service.impl.CategoryServiceImp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryServiceImp categoryService;
    private static final String MESSAGE = "message";

    @PostMapping("/category")
    public ResponseEntity<Object> createCategory(@RequestBody CategoryDTO categoryDTO, Authentication authentication) {
        try {
            String requestId = UUID.randomUUID().toString();
            log.info("JOIN TO TTE-APP with requestId: {}", requestId);

            // Check authorization
            boolean isEmployee = checkAuthorization(authentication);
            categoryDTO.setState(isEmployee ? "Pending" : "Approved");

            // Save category
            ResponseEntity<Object> response = categoryService.saveCategory(categoryDTO, requestId);
            Map<String, Object> responseBody = getStringObjectMap(response);

            return ResponseEntity.status(response.getStatusCode()).body(responseBody);
        } catch (Exception e) {
            log.error("Error creating category: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(MESSAGE, "Failed to create category"));
        }
    }

    private static boolean checkAuthorization(Authentication authentication) {
        // Get the authenticated user's authorities (roles)
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // Check if the user has the "superAdmin" role
        return authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_employee"));
    }

    private static Map<String, Object> getStringObjectMap(ResponseEntity<Object> response) {
        Long categoryId = null;
        if (response.getStatusCode() == HttpStatus.CREATED) {
            categoryId = ((Category) Objects.requireNonNull(response.getBody())).getId();
        }
        Map<String, Object> responseBody = new HashMap<>();
        if (response.getStatusCode() == HttpStatus.CREATED) {
            responseBody.put("categoryId", categoryId);
            responseBody.put(MESSAGE, "Category created successfully");
        } else {
            responseBody.put(MESSAGE, "Failed to create category");
        }
        return responseBody;
    }
}
