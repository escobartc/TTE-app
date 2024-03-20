package com.challenge.tteapp.repository;

import com.challenge.tteapp.model.Category;
import com.challenge.tteapp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
    List<Category> findByState(String state);
}
