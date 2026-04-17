# UrlShortenerApplication

A Spring Boot URL shortener MVP built with:
- MySQL for persistent storage
- Redis for fast short-code lookups (cache)
- Base62 encoding for compact short codes

This is the first version and is designed to evolve toward stronger scalability and security.

## Current MVP Flow
1. Save original URL in MySQL
2. Use generated numeric ID to build a Base62 short code
3. Store and serve short-code lookups via Redis cache (with DB fallback)

## Tech Stack
- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Spring Data Redis
- MySQL
- Maven Wrapper (`mvnw`, `mvnw.cmd`)

## API Endpoints
- `POST /url/api/shorten`
  - Body:
    ```json
    {
      "url": "https://example.com/some/very/long/path"
    }
    ```
  - Response:
    ```json
    {
      "shortUrl": "abc123"
    }
    ```

- `GET /url/{shortCode}`
  - Redirects (`302 FOUND`) to the original URL.

## Prerequisites
- Java 21 installed
- MySQL running locally
- Redis running locally (default host/port expected by Spring Boot)

## Environment Variables
The app reads DB credentials from environment variables:
- `SQL_USERNAME`
- `SQL_PASSWORD`

Database URL is currently configured as:
- `jdbc:mysql://localhost:3306/UrlShortener`

### Set variables on Windows PowerShell
```powershell
$env:SQL_USERNAME="your_mysql_username"
$env:SQL_PASSWORD="your_mysql_password"
```

### Set variables on macOS/Linux
```bash
export SQL_USERNAME="your_mysql_username"
export SQL_PASSWORD="your_mysql_password"
```

## Run Locally
From project root:

```powershell
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```

Default app URL:
- `http://localhost:8080`

## Quick Test
Create a short URL:

```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/url/api/shorten" -ContentType "application/json" -Body '{"url":"https://github.com"}'
```

Open in browser (replace code):
- `http://localhost:8080/url/<shortCode>`

## GitHub Push (after repo init)
If your remote repo is already created on GitHub:

```powershell
git add .
git commit -m "Initial commit: URL shortener MVP"
git remote add origin https://github.com/<your-username>/UrlShortenerApplication.git
git push -u origin main
```

## Roadmap
Planned improvements for next iterations:
- Better short-code generation strategy for high concurrency
- Security hardening (input validation, abuse protection, secure configs)
- Better database/index strategy for scale
- Rate limiting and observability improvements
- More integration and load tests

