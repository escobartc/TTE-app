package com.challenge.tteapp.service.impl;

import com.challenge.tteapp.model.Product;
import com.challenge.tteapp.model.dto.ProductDTO;
import com.challenge.tteapp.processor.ValidationError;
import com.challenge.tteapp.repository.AdminRepository;
import com.challenge.tteapp.repository.ProductRepository;
import com.challenge.tteapp.service.LegacyAdmin;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Slf4j
public class AdminServiceImpl implements LegacyAdmin {

    final AdminRepository adminRepository;
    final ProductRepository productRepository;
    final ValidationError validationError;

    public ResponseEntity<Object> saveProduct(ProductDTO productDTO, String requestId) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        log.info("Save Product in Database {}", requestId);
        // Validate product data if needed
        // Checking if the product title is not empty
        if (product.getTitle() == null || product.getTitle().isEmpty()) {
            log.error("Product title cannot be empty {}", requestId);
            return new ResponseEntity<>(validationError.getStructureError(HttpStatus.BAD_REQUEST.value(),
                    "Product title cannot be empty"), HttpStatus.BAD_REQUEST);
        }

        // Save the product
        Product savedProduct = productRepository.save(product);

        // Return response
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }
}

