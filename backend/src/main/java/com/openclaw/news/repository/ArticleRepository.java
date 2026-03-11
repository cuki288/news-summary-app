package com.openclaw.news.repository;

import com.openclaw.news.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByCategory(String category);

    List<Article> findTop20ByOrderByPublishedAtDesc();

    List<Article> findTop10BySummaryIsNotNullOrderByPublishedAtDesc();

    boolean existsByUrl(String url);

    @Query("SELECT a FROM Article a WHERE a.summary IS NULL OR a.summary = ''")
    List<Article> findUnsummarized();
}
