package com.challenge.tteapp.repository;

import com.challenge.tteapp.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);

    List<Category> findByState(String state);

    @Query("SELECT c FROM Category c WHERE c.state <> 'Approved'")
    List<Category> findAllCategoryOperations();

    @Query("SELECT c FROM Category c WHERE c.id = :idCategory and c.state <> 'Approved'")
    Category findCategoryId(@Param("idCategory") Long idCategory);
}
