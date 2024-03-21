package com.challenge.tteapp.service;

import com.challenge.tteapp.model.dto.ProductDTO;
import com.challenge.tteapp.model.dto.ReviewDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();
    ResponseEntity<Object> saveProduct(ProductDTO product, String requestId);
     List<ReviewDTO> getProductReviews(Long productId);
    ResponseEntity<Object> addProductReview(Long productId, ReviewDTO reviewDTO, String requestId);
    boolean doesProductExist(Long productId);

}
