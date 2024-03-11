package com.challenge.tteapp.repository;

import com.challenge.tteapp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByInventoryAvailableGreaterThan(int available);
}
