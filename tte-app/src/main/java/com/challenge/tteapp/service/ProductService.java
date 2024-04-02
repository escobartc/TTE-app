package com.challenge.tteapp.service;

import com.challenge.tteapp.model.dto.ProductDTO;
import com.challenge.tteapp.model.dto.ReviewDTO;
import com.challenge.tteapp.model.response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductDTO> getAllProducts();

    Optional<ProductDTO> getProduct(Long product_id);

    ResponseEntity<Object> saveProduct(ProductDTO product, String requestId);

    ResponseEntity<List<ReviewDTO>> getProductReviews(Long productId, String requestId);

    ResponseEntity<MessageResponse> addProductReview(Long productId, ReviewDTO reviewDTO, String requestId, String email);

    Page<ProductDTO> getAllProductsWithOrder(String orderBy, Pageable pageable);
}
