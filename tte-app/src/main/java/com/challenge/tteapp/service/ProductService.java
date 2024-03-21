package com.challenge.tteapp.service;

import com.challenge.tteapp.model.dto.ProductDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductDTO> getAllProducts();
    Optional<ProductDTO> getProduct(Long product_id);
    ResponseEntity<Object> saveProduct(ProductDTO product, String requestId);

}
