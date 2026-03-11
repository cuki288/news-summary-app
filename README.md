# 📰 News Summary App

AI-powered news aggregation and summary application. Fetches global and Korean news, then summarizes hot topics using local AI.

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Frontend | React + TypeScript + Vite |
| Backend | Spring Boot 3.2 (Java 17) |
| Database | H2 (in-memory) |
| AI | Ollama (qwen3.5:2b) |
| RSS | Rome Library |

## Features

- **Multi-source RSS**: Reuters, BBC, 연합뉴스, 한국경제
- **Category filter**: Politics, Economy, World, Tech
- **AI Summary**: Local LLM summarizes each article in Korean
- **Auto-refresh**: Hourly news fetch, 5-min summary batch
- **Dark theme UI**: Glassmorphism cards, responsive design

## Quick Start

### Backend
```bash
cd backend
./mvnw spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm run dev
```

### AI (Ollama)
```bash
ollama run qwen3.5:2b
```

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | /api/articles | All articles (optional ?category=) |
| GET | /api/articles/hot | Top 10 with AI summaries |
| POST | /api/summarize | Trigger manual summarization |

## Architecture

```
RSS Feeds → Spring Boot → H2 DB → REST API → React UI
                ↓
         Ollama (qwen3.5:2b)
         AI Summarization
```

## License

MIT
