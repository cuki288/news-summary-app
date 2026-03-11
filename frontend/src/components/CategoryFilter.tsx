import React from 'react';

interface CategoryFilterProps {
  categories: string[];
  selectedCategory: string;
  onCategoryChange: (category: string) => void;
}

const categoryLabels: Record<string, string> = {
  all: '🔥 All',
  politics: '🏛️ Politics',
  economy: '💰 Economy',
  world: '🌍 World',
  tech: '💻 Tech',
};

function CategoryFilter({ categories, selectedCategory, onCategoryChange }: CategoryFilterProps) {
  return (
    <div className="category-filter">
      {categories.map(cat => (
        <button
          key={cat}
          className={`category-btn ${selectedCategory === cat ? 'active' : ''}`}
          onClick={() => onCategoryChange(cat)}
        >
          {categoryLabels[cat] || cat}
        </button>
      ))}
    </div>
  );
}

export default CategoryFilter;
