package com.challenge.tteapp.service;

import com.challenge.tteapp.LoggerPrinter;
import com.challenge.tteapp.model.Product;
import org.springframework.http.ResponseEntity;

public interface LegacyAdmin  {
    ResponseEntity<Object> saveProduct(Product product, LoggerPrinter loggerPrinter);
}
