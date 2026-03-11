package com.openclaw.news.service;

import com.openclaw.news.model.Article;
import com.openclaw.news.repository.ArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SummaryScheduler {
    
    private static final Logger logger = LoggerFactory.getLogger(SummaryScheduler.class);
    
    @Autowired
    private AiSummaryService aiSummaryService;
    
    @Autowired
    private ArticleRepository articleRepository;
    
    @Scheduled(fixedRate = 300000) // Run every 5 minutes (300000 ms)
    public void generateSummaries() {
        logger.info("Starting AI summary generation process...");
        
        try {
            // Find articles without summaries (null or empty)
            List<Article> articlesWithoutSummary = articleRepository.findAll().stream()
                    .filter(article -> article.getSummary() == null || article.getSummary().isEmpty())
                    .limit(10) // Process max 10 per batch
                    .toList();
            
            if (articlesWithoutSummary.isEmpty()) {
                logger.info("No articles without summaries found.");
                return;
            }
            
            logger.info("Processing {} articles for AI summarization.", articlesWithoutSummary.size());
            
            int processedCount = 0;
            for (Article article : articlesWithoutSummary) {
                try {
                    String summary = aiSummaryService.summarize(article.getTitle(), article.getSummary());
                    article.setSummary(summary);
                    articleRepository.save(article);
                    processedCount++;
                } catch (Exception e) {
                    logger.error("Error processing article ID {}: {}", article.getId(), e.getMessage());
                }
            }
            
            logger.info("Successfully processed {} articles for summaries.", processedCount);
            
        } catch (Exception e) {
            logger.error("Error during AI summary generation: " + e.getMessage(), e);
        }
    }
}