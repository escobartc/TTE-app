package com.challenge.tteapp.repository;

import com.challenge.tteapp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SuppressWarnings("SqlNoDataSourceInspection")
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByInventoryAvailableGreaterThan(int available);

    @Query("SELECT w.articleId FROM WishList w WHERE w.user = :userId")
    List<Integer> findArticleIdsByUserId(@Param("userId") Long userId);

    @Query("SELECT id FROM Product")
    List<Integer> findProductById();

    @Query(value = "SELECT p.price FROM product p WHERE p.id = :productId", nativeQuery = true)
    Double findProductPriceById(@Param("productId") Long productId);

    @Query(value = "SELECT i.available  from product p join inventory i on p.id = i.id where p.id =:userId", nativeQuery = true)
    Integer availableProducts(@Param("userId") Long userId);

    @Query("SELECT p FROM Product p WHERE p.id = :idProduct and p.state <> 'Approved'")
    Product findProductId(@Param("idProduct") Long idProduct);

    @Query("SELECT p FROM Product p WHERE p.state <> 'Approved' ")
    List<Product> findAllProductsOperations();
}
