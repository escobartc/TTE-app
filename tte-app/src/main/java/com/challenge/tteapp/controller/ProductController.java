package com.challenge.tteapp.controller;

import com.challenge.tteapp.model.Category;
import com.challenge.tteapp.model.Inventory;
import com.challenge.tteapp.model.Product;
import com.challenge.tteapp.model.Rating;
import com.challenge.tteapp.model.dto.CategoryDTO;
import com.challenge.tteapp.model.dto.InventoryDTO;
import com.challenge.tteapp.model.dto.ProductDTO;
import com.challenge.tteapp.model.dto.RatingDTO;
import com.challenge.tteapp.repository.ProductRepository;
import com.challenge.tteapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;
    private static final String MESSAGE  = "message";


    @GetMapping("/product")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping(path = "/product", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createProduct(@RequestBody ProductDTO product, Authentication authentication) {
        try {
            String requestId = UUID.randomUUID().toString();
            log.info("JOIN TO TTE-APP with requestId: {}", requestId);
            verifyAuthorizationForCreation(product, authentication);
            // Save the product
            ResponseEntity<Object> response = productService.saveProduct(product, requestId);
            Map<String, Object> responseBody = getStringObjectMap(response);
            return ResponseEntity.status(response.getStatusCode()).body(responseBody);
        } catch (Exception e) {
            log.error("Error creating product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(MESSAGE, "Failed to create product"));
        }
    }

    @PutMapping(path = "/product", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateProduct(@RequestBody ProductDTO productDTO) {
        try {
            String requestId = UUID.randomUUID().toString();
            log.info("JOIN TO TTE-APP with requestId: {}", requestId);

            // Check if the product ID is provided
            Long productId = productDTO.getId();
            if (productId == null) {
                return ResponseEntity.badRequest().body(Map.of(MESSAGE, "Product ID is required"));
            }

            // Retrieve the existing product from the database
            Optional<Product> existingProductOptional = productRepository.findById(productId);
            if (existingProductOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Product existingProduct = existingProductOptional.get();

            // Set the product fields for update
            existingProduct = setProductFieldsForUpdate(productDTO, existingProduct);

            // Save the updated product back to the database
            productRepository.save(existingProduct);

            // Return success message
            return ResponseEntity.ok().body(Map.of(MESSAGE, "Updated successfully"));
        } catch (Exception e) {
            log.error("Error updating product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(MESSAGE, "Failed to update product"));
        }
    }
    @DeleteMapping(path = "/product", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteProduct(@RequestBody Map<String, Long> requestBody, Authentication authentication) {
        try {
            Long productId = requestBody.get("id");
            // Get the authenticated user's authorities (roles)
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            // Check if the user has the "superAdmin" role
            boolean isEmployee = authorities.stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_employee"));

            // Retrieve the existing product from the database
            Optional<Product> existingProductOptional = productRepository.findById(productId);
            if (existingProductOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(MESSAGE, "Product with provided ID not found"));
            }

            Product existingProduct = existingProductOptional.get();

            // If the user is a superAdmin, delete the product from the database
            if (!isEmployee) {
                productRepository.deleteById(productId);
                return ResponseEntity.ok().body(Map.of(MESSAGE, "Product deleted successfully"));
            }

            // If the user is an employee, change the state of the product to "Pending"
            existingProduct.setState("Pending for deletion");
            productRepository.save(existingProduct);

            // Return success message
            return ResponseEntity.ok().body(Map.of(MESSAGE, "Product state changed to Pending for deletion"));
        } catch (Exception e) {
            log.error("Error deleting product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(MESSAGE, "Failed to delete product"));
        }
    }

    private static Product setProductFieldsForUpdate(ProductDTO productDTO, Product existingProduct) {
        // Update the product fields with the new values if they are not null in the request body
        if (productDTO.getTitle() != null) {
            existingProduct.setTitle(productDTO.getTitle());
        }
        if (productDTO.getPrice() != null) {
            existingProduct.setPrice(productDTO.getPrice());
        }
        if (productDTO.getDescription() != null) {
            existingProduct.setDescription(productDTO.getDescription());
        }
        if (productDTO.getCategory() != null) {
            // Assuming productDTO.getCategory() returns a CategoryDTO object
            CategoryDTO categoryDTO = productDTO.getCategory();
            Category category = new Category();
            category.setId(categoryDTO.getId()); // Set the ID of the category
            existingProduct.setCategory(category);
        }
        if (productDTO.getImage() != null) {
            existingProduct.setImage(productDTO.getImage());
        }
        if (productDTO.getInventory() != null) {
            InventoryDTO inventoryDTO = productDTO.getInventory();
            Inventory existingInventory = existingProduct.getInventory();
            if (existingInventory == null) {
                existingInventory = new Inventory();
                existingProduct.setInventory(existingInventory);
            }
            existingInventory.setTotal(inventoryDTO.getTotal());
            existingInventory.setAvailable(inventoryDTO.getAvailable());
        }
        if (productDTO.getRating() != null) {
            RatingDTO ratingDTO = productDTO.getRating();
            Rating existingRating = existingProduct.getRating();
            if (existingRating == null) {
                existingRating = new Rating();
                existingProduct.setRating(existingRating);
            }
            existingRating.setRate(ratingDTO.getRate());
            existingRating.setCount(ratingDTO.getCount());
        }
        return existingProduct;
    }


    private static void verifyAuthorizationForCreation(ProductDTO product, Authentication authentication) {
        // Get the authenticated user's authorities (roles)
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // Check if the user has the "employee" role
        boolean isEmployee = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_employee"));

        product.setState((isEmployee) ? "Pending" : "Approved");
    }

    private static Map<String, Object> getStringObjectMap(ResponseEntity<Object> response) {
        Long productId = null;
        if (response.getStatusCode() == HttpStatus.CREATED) {
            productId = ((Product) Objects.requireNonNull(response.getBody())).getId();
        }
        Map<String, Object> responseBody = new HashMap<>();
        if (response.getStatusCode() == HttpStatus.CREATED) {
            responseBody.put("productId", productId);
            responseBody.put(MESSAGE, "Product created successfully");
        } else {
            responseBody.put(MESSAGE, "Failed to create product");
        }
        return responseBody;
    }


}
