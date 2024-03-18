package com.challenge.tteapp.service.impl;

import com.challenge.tteapp.model.Category;
import com.challenge.tteapp.model.Inventory;
import com.challenge.tteapp.model.Product;
import com.challenge.tteapp.model.Rating;
import com.challenge.tteapp.model.dto.CategoryDTO;
import com.challenge.tteapp.model.dto.ProductDTO;
import com.challenge.tteapp.repository.CategoryRepository;
import com.challenge.tteapp.repository.ProductRepository;
import com.challenge.tteapp.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findByInventoryAvailableGreaterThan(0).stream()
                .map(this::mapToProductDTO)
                .toList();
    }

    private ProductDTO mapToProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setTitle(product.getTitle());
        productDTO.setPrice(product.getPrice());
        productDTO.setCategory(mapToCategoryDTO(product.getCategory())); // Map Category to CategoryDTO
        return productDTO;
    }

    private CategoryDTO mapToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }

    public ResponseEntity<Object> saveProduct(ProductDTO productDTO, String requestId) {
        Product product = copyProductForDB(productDTO);

        // Resolve category based on name and set it in the product
        Category category = categoryRepository.findByName(productDTO.getCategory().getName());
        if (category == null) {
            // If category does not exist, create it
            category = new Category();
            category.setName(productDTO.getCategory().getName());
            category.setState(productDTO.getState());
            category = categoryRepository.save(category);
        }
        product.setCategory(category);

        // Save the Product entity
        Product savedProduct = productRepository.save(product);

        // Return ResponseEntity with the saved Product
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    private static Product copyProductForDB(ProductDTO productDTO) {
        // Create a new Product entity
        Product product = new Product();

        // Set simple properties directly
        product.setTitle(productDTO.getTitle());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setImage(productDTO.getImage());
        product.setState(productDTO.getState());

        // Create Rating entity and set properties
        Rating rating = new Rating();
        rating.setRate(productDTO.getRating().getRate());
        rating.setCount(productDTO.getRating().getCount());

        // Create Inventory entity and set properties
        Inventory inventory = new Inventory();
        inventory.setTotal(productDTO.getInventory().getTotal());
        inventory.setAvailable(productDTO.getInventory().getAvailable());

        // Set Rating and Inventory entities to the Product
        product.setRating(rating);
        product.setInventory(inventory);
        return product;
    }
}
