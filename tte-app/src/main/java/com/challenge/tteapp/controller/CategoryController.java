package com.challenge.tteapp.controller;

import com.challenge.tteapp.configuration.InfoToken;
import com.challenge.tteapp.model.dto.CategoryDTO;
import com.challenge.tteapp.model.response.AllCategoriesResponse;
import com.challenge.tteapp.model.response.CategoryResponse;
import com.challenge.tteapp.model.response.MessageResponse;
import com.challenge.tteapp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/category")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryDTO categoryDTO) {
        String requestId = UUID.randomUUID().toString();
        String role = InfoToken.getRole();
        log.info("JOIN TO TTE-APP, creation category with requestId: [{}]", requestId);
        return categoryService.createCategory(categoryDTO, role, requestId);
    }

    @GetMapping("/category")
    public ResponseEntity<List<AllCategoriesResponse>> getAllCategories() {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, view all categories with requestId: [{}]", requestId);
        return categoryService.getAllCategories(requestId);
    }

    @DeleteMapping(path = "/category", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> deleteCategory(@RequestBody Map<String, Long> requestBody) {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, delete categories with requestId: [{}]", requestId);
        String role = InfoToken.getRole();
        return categoryService.deleteCategory(requestBody, role,requestId);
    }

//    @PutMapping(path = "/category", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)

}
