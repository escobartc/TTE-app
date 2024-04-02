package com.challenge.tteapp.controller;

import com.challenge.tteapp.configuration.InfoToken;
import com.challenge.tteapp.model.Category;
import com.challenge.tteapp.model.Inventory;
import com.challenge.tteapp.model.Product;
import com.challenge.tteapp.model.Rating;
import com.challenge.tteapp.model.dto.*;
import com.challenge.tteapp.model.response.MessageResponse;
import com.challenge.tteapp.repository.ProductRepository;
import com.challenge.tteapp.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.challenge.tteapp.model.Constants.MESSAGE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @GetMapping("/store/product/{productId}/reviews")
    public ResponseEntity<List<ReviewDTO>> getProductReviews(@PathVariable Long productId) {
        String requestId = UUID.randomUUID().toString();
        log.info("JOIN TO TTE-APP, products reviews with requestId: [{}]", requestId);
        return productService.getProductReviews(productId, requestId);
    }

    @PostMapping("/store/product/{productId}/reviews/add")
    public ResponseEntity<MessageResponse> addProductReview(@PathVariable Long productId, @RequestBody @Valid ReviewDTO reviewDTO) {
        String requestId = UUID.randomUUID().toString();
        String email = InfoToken.getName();
        log.info("JOIN TO TTE-APP add product review, with requestId: [{}]", requestId);
        return productService.addProductReview(productId, reviewDTO, requestId,email);
    }


    @GetMapping("/product")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/store/products/{product_id}")
    public ResponseEntity<Object> productDetail(@PathVariable Long product_id) {
        Optional<ProductDTO> productOptional = productService.getProduct(product_id);
        if (productOptional.isPresent()) {
            ProductDTO product = productOptional.get();
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            String errorMessage = "Product with id " + product_id + " not found";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", errorMessage)); // Return 404 Not Found with custom message
        }
    }

    @PostMapping(path = "/product", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createProduct(@RequestBody ProductDTO product, Authentication authentication) {
        try {
            String requestId = UUID.randomUUID().toString();
            log.info("JOIN TO TTE-APP with requestId: [{}]", requestId);
            verifyAuthorizationForCreation(product, authentication);
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
            log.info("JOIN TO TTE-APP with requestId: [{}]", requestId);

            Long productId = productDTO.getId();
            if (productId == null) {
                return ResponseEntity.badRequest().body(Map.of(MESSAGE, "Product ID is required"));
            }

            Optional<Product> existingProductOptional = productRepository.findById(productId);
            if (existingProductOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Product existingProduct = existingProductOptional.get();

            existingProduct = setProductFieldsForUpdate(productDTO, existingProduct);

            productRepository.save(existingProduct);

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
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            boolean isEmployee = authorities.stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_employee"));

            Optional<Product> existingProductOptional = productRepository.findById(productId);
            if (existingProductOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(MESSAGE, "Product with provided ID not found"));
            }

            Product existingProduct = existingProductOptional.get();

            if (!isEmployee) {
                productRepository.deleteById(productId);
                return ResponseEntity.ok().body(Map.of(MESSAGE, "Product deleted successfully"));
            }

            existingProduct.setState("Pending for deletion");
            productRepository.save(existingProduct);

            return ResponseEntity.ok().body(Map.of(MESSAGE, "Product state changed to Pending for deletion"));
        } catch (Exception e) {
            log.error("Error deleting product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(MESSAGE, "Failed to delete product"));
        }
    }

    @GetMapping("/store/products")
    public ResponseEntity<Page<ProductDTO>> getProductsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String orderBy) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ProductDTO> products = productService.getAllProductsWithOrder(orderBy, pageRequest);

        return ResponseEntity.ok(products);
    }


    private static Product setProductFieldsForUpdate(ProductDTO productDTO, Product existingProduct) {

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
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

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
