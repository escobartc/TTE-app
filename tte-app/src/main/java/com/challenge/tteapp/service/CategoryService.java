package com.challenge.tteapp.service;

import com.challenge.tteapp.model.dto.CategoryDTO;
import com.challenge.tteapp.model.dto.CategoryUpdate;
import com.challenge.tteapp.model.response.AllCategoriesResponse;
import com.challenge.tteapp.model.response.CategoryResponse;
import com.challenge.tteapp.model.response.MessageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    ResponseEntity<List<AllCategoriesResponse>> getAllCategories(String requestId);

    ResponseEntity<CategoryResponse> createCategory(CategoryDTO categoryDTO, String role, String requestId);

    ResponseEntity<MessageResponse> deleteCategory(Map<String, Long> requestBody, String role,String requestId);
    ResponseEntity<MessageResponse> updateCategory(CategoryUpdate categoryDTO, String requestId);

}
