package com.challenge.tteapp.service;

import com.challenge.tteapp.model.dto.ProductDTO;
import org.springframework.http.ResponseEntity;

public interface LegacyAdmin  {
    ResponseEntity<Object> saveProduct(ProductDTO product, String requestId);
}
