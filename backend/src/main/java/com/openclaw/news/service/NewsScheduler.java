package com.openclaw.news.service;

import com.openclaw.news.model.Article;
import com.openclaw.news.repository.ArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NewsScheduler {
    
    private static final Logger logger = LoggerFactory.getLogger(NewsScheduler.class);
    
    @Autowired
    private RssFeedService rssFeedService;
    
    @Autowired
    private ArticleRepository articleRepository;
    
    @Scheduled(fixedRate = 3600000) // Run every hour (3600000 ms = 1 hour)
    public void fetchNews() {
        logger.info("Starting news fetching process...");
        
        try {
            List<Article> newArticles = rssFeedService.fetchAndParseFeeds();
            
            if (newArticles.isEmpty()) {
                logger.info("No new articles fetched.");
                return;
            }
            
            // Filter out duplicates by URL
            List<String> existingUrls = articleRepository.findAll().stream()
                    .map(Article::getUrl)
                    .collect(Collectors.toList());
            
            List<Article> uniqueArticles = newArticles.stream()
                    .filter(article -> !existingUrls.contains(article.getUrl()))
                    .collect(Collectors.toList());
            
            if (!uniqueArticles.isEmpty()) {
                articleRepository.saveAll(uniqueArticles);
                logger.info("Successfully fetched and saved {} new articles.", uniqueArticles.size());
            } else {
                logger.info("No new articles to save (all were duplicates).");
            }
            
        } catch (Exception e) {
            logger.error("Error during news fetching: " + e.getMessage(), e);
        }
    }
}