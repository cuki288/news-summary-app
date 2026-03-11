package com.openclaw.news.controller;

import com.openclaw.news.model.Article;
import com.openclaw.news.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    
    @Autowired
    private ArticleRepository articleRepository;
    
    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles(@RequestParam(required = false) String category) {
        if (category != null && !category.isEmpty()) {
            List<Article> articles = articleRepository.findByCategory(category);
            return ResponseEntity.ok(articles);
        } else {
            List<Article> articles = articleRepository.findTop20ByOrderByPublishedAtDesc();
            return ResponseEntity.ok(articles);
        }
    }
}