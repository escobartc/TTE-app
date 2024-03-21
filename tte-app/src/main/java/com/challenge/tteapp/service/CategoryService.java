package com.challenge.tteapp.service;

import com.challenge.tteapp.model.dto.CategoryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();

    ResponseEntity<Object> saveCategory(CategoryDTO categoryDTO, String requestId);

    ResponseEntity<Object> deleteCategory(Map<String, Long> requestBody, Authentication authentication);
}
