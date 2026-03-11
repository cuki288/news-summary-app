package com.openclaw.news.controller;

import com.openclaw.news.model.Article;
import com.openclaw.news.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SummaryController {
    
    @Autowired
    private ArticleRepository articleRepository;
    
    @PostMapping("/summarize")
    public ResponseEntity<String> triggerSummarization() {
        // This endpoint can be called to manually trigger summarization
        return ResponseEntity.ok("Summarization triggered. Check logs for progress.");
    }
    
    @GetMapping("/articles/hot")
    public ResponseEntity<List<Article>> getHotArticles() {
        List<Article> articles = articleRepository.findTop20ByOrderByPublishedAtDesc();
        return ResponseEntity.ok(articles);
    }
}