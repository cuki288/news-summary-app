import React from 'react';

interface Article {
  id: number;
  title: string;
  summary: string;
  source: string;
  category: string;
  url: string;
  publishedAt: string;
}

const categoryColors: Record<string, string> = {
  politics: '#ef4444',
  economy: '#f59e0b',
  world: '#3b82f6',
  tech: '#10b981',
};

function timeAgo(dateStr: string): string {
  const diff = Date.now() - new Date(dateStr).getTime();
  const hours = Math.floor(diff / 3600000);
  if (hours < 1) return 'Just now';
  if (hours < 24) return `${hours}h ago`;
  return `${Math.floor(hours / 24)}d ago`;
}

function ArticleCard({ article }: { article: Article }) {
  return (
    <div className="article-card" onClick={() => window.open(article.url, '_blank')}>
      <span
        className="category-badge"
        style={{ backgroundColor: categoryColors[article.category] || '#6366f1' }}
      >
        {article.category}
      </span>
      <h3 className="article-title">{article.title}</h3>
      <p className="article-summary">{article.summary || 'Summary pending...'}</p>
      <div className="article-footer">
        <span className="article-source">{article.source}</span>
        <span className="article-time">{timeAgo(article.publishedAt)}</span>
      </div>
    </div>
  );
}

export default ArticleCard;
