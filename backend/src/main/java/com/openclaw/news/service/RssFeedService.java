package com.openclaw.news.service;

import com.openclaw.news.model.Article;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class RssFeedService {

    private static final Logger log = LoggerFactory.getLogger(RssFeedService.class);

    private static final Map<String, String> FEED_SOURCES = new LinkedHashMap<>() {{
        put("https://feeds.bbci.co.uk/news/rss.xml", "world");
        put("https://feeds.bbci.co.uk/news/politics/rss.xml", "politics");
        put("https://feeds.bbci.co.uk/news/technology/rss.xml", "tech");
        put("https://feeds.reuters.com/reuters/worldNews", "world");
        put("https://www.yna.co.kr/RSS/news.xml", "world");
        put("https://www.hankyung.com/feed/all-news", "economy");
    }};

    public List<Article> fetchAllFeeds() {
        List<Article> articles = new ArrayList<>();

        for (Map.Entry<String, String> entry : FEED_SOURCES.entrySet()) {
            try {
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(new XmlReader(new URL(entry.getKey())));
                String category = entry.getValue();
                String source = feed.getTitle() != null ? feed.getTitle() : entry.getKey();

                for (SyndEntry item : feed.getEntries()) {
                    Article article = new Article();
                    article.setTitle(item.getTitle());
                    article.setUrl(item.getLink());
                    article.setSource(source);
                    article.setCategory(category);

                    if (item.getDescription() != null) {
                        String desc = item.getDescription().getValue();
                        article.setSummary(null); // Will be filled by AI
                    }

                    if (item.getPublishedDate() != null) {
                        article.setPublishedAt(LocalDateTime.ofInstant(
                                item.getPublishedDate().toInstant(), ZoneId.systemDefault()));
                    } else {
                        article.setPublishedAt(LocalDateTime.now());
                    }

                    article.setCreatedAt(LocalDateTime.now());
                    articles.add(article);
                }

                log.info("Fetched {} articles from {}", feed.getEntries().size(), source);
            } catch (Exception e) {
                log.warn("Failed to fetch feed {}: {}", entry.getKey(), e.getMessage());
            }
        }

        return articles;
    }
}
