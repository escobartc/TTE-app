package com.challenge.tteapp.repository;

import com.challenge.tteapp.model.Category;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);

    List<Category> findByState(String state);

    @Query("SELECT c FROM Category c WHERE c.state <> 'APPROVED'")
    List<Category> findAllCategoryOperations();

    @Query("SELECT c FROM Category c WHERE c.id = :idCategory and c.state <> 'APPROVED'")
    Category findCategoryId(@Param("idCategory") Long idCategory);

    @Transactional
    @Modifying
    @Query("UPDATE Category c SET c.name =:nameUpdate where c.id=:categoryId")
    void updateCategory(@Param("categoryId") Long categoryId, @Param("nameUpdate") String nameUpdate);
}
