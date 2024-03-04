package com.endava.tteapp.repository;

import com.endava.tteapp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Define specific methods for product management if needed
}
