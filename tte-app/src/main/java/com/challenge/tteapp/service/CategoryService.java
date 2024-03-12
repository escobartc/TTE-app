package com.challenge.tteapp.service;

import com.challenge.tteapp.model.dto.CategoryDTO;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    ResponseEntity<Object> saveCategory(CategoryDTO categoryDTO, String requestId);
}
