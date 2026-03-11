package com.openclaw.news.controller;

import com.openclaw.news.model.Article;
import com.openclaw.news.repository.ArticleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin(origins = "*")
public class ArticleController {

    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping
    public List<Article> getArticles(@RequestParam(required = false) String category) {
        if (category != null && !category.isEmpty()) {
            return articleRepository.findByCategory(category);
        }
        return articleRepository.findTop20ByOrderByPublishedAtDesc();
    }

    @GetMapping("/hot")
    public List<Article> getHotArticles() {
        return articleRepository.findTop10BySummaryIsNotNullOrderByPublishedAtDesc();
    }
}
