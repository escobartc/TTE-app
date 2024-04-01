package com.challenge.tteapp.service.impl;

import com.challenge.tteapp.model.Category;
import com.challenge.tteapp.model.dto.CategoryDTO;
import com.challenge.tteapp.model.response.AllCategoriesResponse;
import com.challenge.tteapp.model.response.CategoryResponse;
import com.challenge.tteapp.model.response.MessageResponse;
import com.challenge.tteapp.model.response.StatusResponse;
import com.challenge.tteapp.repository.CategoryRepository;
import com.challenge.tteapp.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.swing.plaf.TreeUI;
import java.util.*;

@AllArgsConstructor
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public ResponseEntity<List<AllCategoriesResponse>> getAllCategories(String requestId) {
        log.info("view all categories with requestId: [{}]", requestId);
        List<Category> categoriesResponse = categoryRepository.findByState("APPROVED");
        List<AllCategoriesResponse> buildCategoryResponse = mapToCategoryDTO(categoriesResponse);
        return new ResponseEntity<>(buildCategoryResponse, HttpStatus.CREATED);
    }
    @Override
    public ResponseEntity<CategoryResponse> createCategory(CategoryDTO categoryDTO, String role, String requestId) {
        log.info("creation category, with requestId: [{}]", requestId);
        if (categoryRepository.findByName(categoryDTO.getName()) != null) {
            log.warn("Category with this name already exists, with requestId: [{}]", requestId);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Category with this name already exists");
        }
        Category category = new Category();
        category.setName(categoryDTO.getName());
        boolean roleState = (role.equals("ADMIN") ? Boolean.TRUE: Boolean.FALSE);
        category.setState(roleState ? "APPROVED" : "PENDING");
        Category savedCategory = categoryRepository.save(category);
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setCategoryId(savedCategory.getId());
        categoryResponse.setMessage("Successful");
        log.info("creation category successful, with requestId: [{}]", requestId);
        return new ResponseEntity<>(categoryResponse, HttpStatus.CREATED);
    }
    @Override
    public ResponseEntity<MessageResponse> deleteCategory(Map<String, Long> requestBody, String role,String requestId) {
            Long categoryId = requestBody.get("id");
            boolean isEmployee = (role.equals("ADMIN")? Boolean.TRUE : Boolean.FALSE);
            Optional<Category> existingCategoryOptional = categoryRepository.findById(categoryId);
            if (existingCategoryOptional.isEmpty()) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Category with provided ID not found");
            }
            Category existingCategory = existingCategoryOptional.get();
            if (isEmployee) {
                categoryRepository.deleteById(categoryId);
                return new ResponseEntity<>(new MessageResponse("Category deleted successfully"), HttpStatus.OK);
            }
            else {
                existingCategory.setState("Pending for deletion");
                categoryRepository.save(existingCategory);
                return new ResponseEntity<>(new MessageResponse("Category state changed to Pending for deletion"), HttpStatus.OK);
            }
    }

    private static List<AllCategoriesResponse> mapToCategoryDTO(List<Category> categoriesResponse) {
        List<AllCategoriesResponse> buildCategoryResponse = new ArrayList<>();
        for(Category element: categoriesResponse){
            AllCategoriesResponse allCategoriesResponse = new AllCategoriesResponse();
            allCategoriesResponse.setName(element.getName());
            allCategoriesResponse.setId(element.getId());
            buildCategoryResponse.add(allCategoriesResponse);
        }
        return buildCategoryResponse;
    }
}
