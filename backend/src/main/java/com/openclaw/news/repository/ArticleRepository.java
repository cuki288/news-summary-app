package com.openclaw.news.repository;

import com.openclaw.news.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    
    @Query("SELECT a FROM Article a WHERE a.category = :category ORDER BY a.publishedAt DESC")
    List<Article> findByCategory(@Param("category") String category);
    
    @Query("SELECT a FROM Article a ORDER BY a.publishedAt DESC")
    List<Article> findTop20ByOrderByPublishedAtDesc();
}