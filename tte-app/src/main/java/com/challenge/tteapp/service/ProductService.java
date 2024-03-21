package com.challenge.tteapp.service;

import com.challenge.tteapp.model.dto.ProductDTO;
import com.challenge.tteapp.model.dto.ReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductDTO> getAllProducts();

    Optional<ProductDTO> getProduct(Long product_id);

    ResponseEntity<Object> saveProduct(ProductDTO product, String requestId);

    List<ReviewDTO> getProductReviews(Long productId);

    ResponseEntity<Object> addProductReview(Long productId, ReviewDTO reviewDTO, String requestId);

    boolean doesProductExist(Long productId);

    Page<ProductDTO> getAllProductsWithOrder(String orderBy, Pageable pageable);
}
