package com.challenge.tteapp;

import com.challenge.tteapp.controller.AdminController;
import com.challenge.tteapp.controller.CategoryController;
import com.challenge.tteapp.model.Category;
import com.challenge.tteapp.model.dto.CategoryDTO;
import com.challenge.tteapp.model.dto.CategoryUpdate;
import com.challenge.tteapp.model.response.*;
import com.challenge.tteapp.repository.CategoryRepository;
import com.challenge.tteapp.service.CategoryService;
import com.challenge.tteapp.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {
    @InjectMocks
    private CategoryController categoryController;
    @InjectMocks
    private CategoryServiceImpl categoryServiceimpl;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryService categoryService;

    @Test
    void createCategory() {

        ResponseEntity<CategoryResponse> successResponse = new ResponseEntity<>(new CategoryResponse(), HttpStatus.CREATED);
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("name");
        lenient().when(categoryService.createCategory(eq(categoryDTO), eq("ADMIN"), anyString())).thenReturn(successResponse);
        categoryController.createCategory(categoryDTO);
        Category category = new Category();
        category.setId(1L);
        category.setState("state");
        category.setName("name");
        lenient().when(categoryRepository.save(any(Category.class))).thenReturn(category);
        ResponseEntity<CategoryResponse> response = categoryServiceimpl.createCategory(categoryDTO, "ADMIN", "requestId");
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        when(categoryRepository.findByName(anyString())).thenReturn(new Category());
        assertThrows(HttpClientErrorException.class, () -> {
            categoryServiceimpl.createCategory(categoryDTO, "ADMIN", "requestId");
        });
    }

    @Test
    void getAllCategoriesTest() {
        ResponseEntity<List<AllCategoriesResponse>> successResponse = new ResponseEntity<>(new ArrayList<>(), HttpStatus.CREATED);
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("name");
        lenient().when(categoryService.getAllCategories(anyString())).thenReturn(successResponse);
        categoryController.getAllCategories();
        Category category = new Category();
        List<Category> categories = new ArrayList<>();
        categories.add(category);
        when(categoryRepository.findByState(anyString())).thenReturn(categories);
        ResponseEntity<List<AllCategoriesResponse>> response = categoryServiceimpl.getAllCategories("requestId");
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void deleteCategoryTest(){
        ResponseEntity<MessageResponse> successResponse = new ResponseEntity<>(new MessageResponse(), HttpStatus.CREATED);
        Map<String, Long> stringMap = new HashMap<>();
        stringMap.put("id", 1L);
        lenient().when(categoryService.deleteCategory(eq(stringMap),anyString(),anyString())).thenReturn(successResponse);
        categoryController.deleteCategory(stringMap);
        assertThrows(HttpClientErrorException.class, () -> {
            categoryServiceimpl.deleteCategory(stringMap, "ADMIN", "requestId");
        });
        Category category = new Category();
        category.setId(1L);
        category.setName("name");
        category.setState("state");
        Optional<Category> category1 = Optional.of(category);
        lenient().when(categoryRepository.findById(eq(1L))).thenReturn(category1);

        ResponseEntity<MessageResponse> response = categoryServiceimpl.deleteCategory(stringMap, "ADMIN", "requestId");
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseEntity<MessageResponse> response2 = categoryServiceimpl.deleteCategory(stringMap, "EMPLOYEE", "requestId");
        assertEquals(HttpStatus.OK, response2.getStatusCode());
    }


    @Test
    void updateCategoryTest(){
        ResponseEntity<MessageResponse> successResponse = new ResponseEntity<>(new MessageResponse(), HttpStatus.CREATED);
        CategoryUpdate categoryUpdate = new CategoryUpdate();
        categoryUpdate.setId(1L);
        categoryUpdate.setName("name");

        lenient().when(categoryService.updateCategory(eq(categoryUpdate),anyString())).thenReturn(successResponse);
        categoryController.updateCategory(categoryUpdate);
        assertThrows(HttpClientErrorException.class, () -> {
            categoryServiceimpl.updateCategory(categoryUpdate, "requestId");
        });
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(new Category()));
        ResponseEntity<MessageResponse> response = categoryServiceimpl.updateCategory(categoryUpdate, "requestId");
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }
}
