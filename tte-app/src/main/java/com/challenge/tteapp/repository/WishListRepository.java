package com.challenge.tteapp.repository;

import com.challenge.tteapp.model.WishList;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface WishListRepository extends JpaRepository<WishList,Long> {

    @Query("SELECT w.articleId FROM WishList w WHERE w.user = :userId")
    List<Integer> findArticleIdsByUserId(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM WishList w WHERE w.user = :userId AND w.articleId = :articleId")
    void deleteByUserIdAndArticleId(@Param("userId") Long userId, @Param("articleId") Integer articleId);
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO wishlist (user_id, product_id) VALUES (:userId, :articleId)", nativeQuery = true)
    void addElementToList(@Param("userId") Long userId, @Param("articleId") Integer articleId);
}
