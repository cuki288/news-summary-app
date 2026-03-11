package com.openclaw.news.service;

import com.openclaw.news.model.Article;
import com.openclaw.news.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    
    @Autowired
    private ArticleRepository articleRepository;
    
    public List<Article> getTop10ArticlesWithSummary() {
        return articleRepository.findTop20ByOrderByPublishedAtDesc();
    }
}