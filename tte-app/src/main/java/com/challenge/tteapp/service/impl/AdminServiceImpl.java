package com.challenge.tteapp.service.impl;

import com.challenge.tteapp.LoggerPrinter;
import com.challenge.tteapp.model.Product;
import com.challenge.tteapp.processor.ValidationError;
import com.challenge.tteapp.repository.AdminRepository;
import com.challenge.tteapp.repository.ProductRepository;
import com.challenge.tteapp.service.LegacyAdmin;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class AdminServiceImpl implements LegacyAdmin {

    final AdminRepository adminRepository;
    final ProductRepository productRepository;
    final ValidationError validationError;

    public ResponseEntity<Object> saveProduct(Product product, LoggerPrinter loggerPrinter) {
        loggerPrinter.log(LogLevel.INFO, "Save Product in Database");
        if (product.getTitle() == null || product.getTitle().isEmpty()) {
            loggerPrinter.log(LogLevel.ERROR, "Product title cannot be empty");
            return new ResponseEntity<>(validationError.getStructureError(HttpStatus.BAD_REQUEST.value(),
                    "Product title cannot be empty"), HttpStatus.BAD_REQUEST);
        }
        Product savedProduct = productRepository.save(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }
}

