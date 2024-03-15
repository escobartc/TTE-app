package com.challenge.tteapp.service.impl;

import com.challenge.tteapp.model.Category;
import com.challenge.tteapp.model.dto.CategoryDTO;
import com.challenge.tteapp.repository.CategoryRepository;
import com.challenge.tteapp.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Slf4j
public class CategoryServiceImp implements CategoryService {
    private final CategoryRepository categoryRepository;
    private static final String MESSAGE = "message";


    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findByState("Approved").stream()
                .map(this::mapToCategoryDTO)
                .toList();
    }

    @Override
    public ResponseEntity<Object> saveCategory(CategoryDTO categoryDTO, String requestId) {
        // Check if category name is provided
        String categoryName = categoryDTO.getName();
        if (categoryName == null || categoryName.isEmpty()) {
            return ResponseEntity.badRequest().body("Category name is required");
        }

        // Check if category already exists
        if (categoryRepository.findByName(categoryName) != null) {
            return ResponseEntity.badRequest().body("Category with this name already exists");
        }

        // Create new category entity
        Category category = new Category();
        category.setName(categoryName);
        category.setState(categoryDTO.getState());

        // Save the category
        Category savedCategory = categoryRepository.save(category);

        // Return response with the saved category
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    @Override
    public ResponseEntity<Object> deleteCategory(Map<String, Long> requestBody, Authentication authentication) {
        try {
            Long categoryId = requestBody.get("id");
            // Get the authenticated user's authorities (roles)
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            // Check if the user has the "superAdmin" role
            boolean isEmployee = authorities.stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_employee"));

            // Retrieve the existing category from the database
            Optional<Category> existingCategoryOptional = categoryRepository.findById(categoryId);
            if (existingCategoryOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(MESSAGE, "Category with provided ID not found"));
            }

            Category existingCategory = existingCategoryOptional.get();

            // If the user is a superAdmin, delete the product from the database
            if (!isEmployee) {
                categoryRepository.deleteById(categoryId);
                return ResponseEntity.ok().body(Map.of(MESSAGE, "Category deleted successfully"));
            }

            // If the user is an employee, change the state of the category to "Pending"
            existingCategory.setState("Pending for deletion");
            categoryRepository.save(existingCategory);

            // Return success message
            return ResponseEntity.ok().body(Map.of(MESSAGE, "Category state changed to Pending for deletion"));
        } catch (Exception e) {
            log.error("Error deleting category: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(MESSAGE, "Failed to delete category"));
        }
    }

    private CategoryDTO mapToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setState(category.getState());
        return categoryDTO;
    }
}
