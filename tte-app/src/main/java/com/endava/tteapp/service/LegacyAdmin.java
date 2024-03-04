package com.endava.tteapp.service;

import com.endava.tteapp.LoggerPrinter;
import com.endava.tteapp.model.Product;
import com.endava.tteapp.model.User;
import org.springframework.http.ResponseEntity;

public interface LegacyAdmin  {
    ResponseEntity<Object> saveUser(User user, LoggerPrinter loggerPrinter);
    ResponseEntity<Object> saveProduct(Product product, LoggerPrinter loggerPrinter);
}
