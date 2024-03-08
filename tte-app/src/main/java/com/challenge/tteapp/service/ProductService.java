package com.challenge.tteapp.service;

import com.challenge.tteapp.model.dto.ProductDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();
    ResponseEntity<Object> saveProduct(ProductDTO product, String requestId);

}
