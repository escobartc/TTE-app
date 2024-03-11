package com.challenge.tteapp.controller;

import com.challenge.tteapp.model.Product;
import com.challenge.tteapp.model.dto.ProductDTO;
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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ProductController {

    private final ProductService productService;

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
            verifyAuthorization(product, authentication);
            // Save the product
            ResponseEntity<Object> response = productService.saveProduct(product, requestId);
            Map<String, Object> responseBody = getStringObjectMap(response);
            return ResponseEntity.status(response.getStatusCode()).body(responseBody);
        } catch (Exception e) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Failed to create product");
            log.error("Error creating product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }

    private static void verifyAuthorization(ProductDTO product, Authentication authentication) {
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
            responseBody.put("message", "Product created successfully");
        } else {
            responseBody.put("message", "Failed to create product");
        }
        return responseBody;
    }


}
