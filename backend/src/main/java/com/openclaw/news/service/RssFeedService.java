package com.openclaw.news.service;

import com.openclaw.news.model.Article;
import com.openclaw.news.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.model.SyndEntry;
import com.rometools.rome.model.SyndFeed;

@Service
public class RssFeedService {
    
    @Autowired
    private ArticleRepository articleRepository;
    
    // Define feed sources with their categories
    private static final Map<String, String> FEED_SOURCES = new HashMap<>();
    static {
        FEED_SOURCES.put("https://feeds.reuters.com/reuters/worldNews", "world");
        FEED_SOURCES.put("https://feeds.bbci.co.uk/news/rss.xml", "world");
        FEED_SOURCES.put("https://www.yna.co.kr/RSS/news.xml", "world");
        FEED_SOURCES.put("https://www.hankyung.com/feed/all-news", "economy");
    }
    
    public List<Article> fetchAndParseFeeds() {
        List<Article> articles = new ArrayList<>();
        
        for (Map.Entry<String, String> entry : FEED_SOURCES.entrySet()) {
            String feedUrl = entry.getKey();
            String category = entry.getValue();
            
            try {
                SyndFeed feed = fetchFeed(feedUrl);
                if (feed != null) {
                    List<Article> parsedArticles = parseFeedEntries(feed, category);
                    articles.addAll(parsedArticles);
                }
            } catch (Exception e) {
                System.err.println("Error fetching or parsing feed from " + feedUrl + ": " + e.getMessage());
                // Continue with other feeds instead of failing completely
            }
        }
        
        return articles;
    }
    
    private SyndFeed fetchFeed(String feedUrl) throws IOException, FeedException {
        URL url = new URL(feedUrl);
        SyndFeedInput input = new SyndFeedInput();
        return input.build(url.openStream());
    }
    
    private List<Article> parseFeedEntries(SyndFeed feed, String category) {
        List<Article> articles = new ArrayList<>();
        
        if (feed.getEntries() == null) {
            return articles;
        }
        
        for (SyndEntry entry : feed.getEntries()) {
            try {
                String title = entry.getTitle();
                String link = entry.getLink();
                String description = entry.getDescription() != null ? entry.getDescription().getValue() : "";
                
                // Parse publication date
                ZonedDateTime publishedDate = entry.getPublishedDate() != null 
                    ? entry.getPublishedDate().toInstant().atZone(ZoneId.systemDefault()) 
                    : LocalDateTime.now().atZone(ZoneId.systemDefault());
                
                // Create article object
                Article article = new Article();
                article.setTitle(title);
                article.setSummary(description);
                article.setSource(feed.getTitle());
                article.setCategory(category);
                article.setUrl(link);
                article.setPublishedAt(publishedDate.toLocalDateTime());
                article.setCreatedAt(LocalDateTime.now());
                
                articles.add(article);
            } catch (Exception e) {
                System.err.println("Error parsing entry: " + e.getMessage());
                // Continue with other entries instead of failing completely
            }
        }
        
        return articles;
    }
}