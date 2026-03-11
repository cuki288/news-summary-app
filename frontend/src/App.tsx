import React, { useState, useEffect } from 'react';
import './App.css';
import ArticleCard from './components/ArticleCard';
import CategoryFilter from './components/CategoryFilter';

interface Article {
  id: number;
  title: string;
  summary: string;
  source: string;
  category: string;
  url: string;
  publishedAt: string;
}

function App() {
  const [articles, setArticles] = useState<Article[]>([]);
  const [selectedCategory, setSelectedCategory] = useState<string>('all');
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    fetch('/api/articles/hot')
      .then(r => r.json())
      .then(setArticles)
      .finally(() => setLoading(false));
  }, []);

  const categories = ['all', 'politics', 'economy', 'world', 'tech'];
  const filtered = selectedCategory === 'all' ? articles : articles.filter(a => a.category === selectedCategory);

  return (
    <div className="App">
      <header className="App-header">
        <h1>📰 News Summary</h1>
        <p className="App-subtitle">AI-powered hot topic summaries</p>
      </header>
      <CategoryFilter
        categories={categories}
        selectedCategory={selectedCategory}
        onCategoryChange={setSelectedCategory}
      />
      {loading ? (
        <div className="loading-spinner">
          <div className="spinner"></div>
          <p>Loading news...</p>
        </div>
      ) : filtered.length === 0 ? (
        <div className="empty-state">No articles found</div>
      ) : (
        <div className="articles-grid">
          {filtered.map(article => (
            <ArticleCard key={article.id} article={article} />
          ))}
        </div>
      )}
    </div>
  );
}

export default App;
