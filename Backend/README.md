# EnglishLog Backend — Java/Spring

Java 21 + Spring Boot 3.3 + Spring Web + Spring Data JPA + Spring Security + JWT + BCrypt + OAuth2 client + PostgreSQL/H2 + OpenAPI. This replaces the previous ASP.NET implementation and is closer to your Android Java/Kotlin background while covering Darwin Application/Integration JD keywords.

## Run locally
Requires JDK 21 and Maven 3.9+.
```bash
mvn spring-boot:run
```
The default embedded H2 database starts on `http://localhost:5050`; Swagger is `/swagger` and OpenAPI is `/v3/api-docs`. Web and Android use the same API URL.

## Run with PostgreSQL
```bash
docker compose up --build
```
The service is on `http://localhost:5050`. Override `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`, and `UPLOAD_DIR` in production.

## API
`POST /api/auth/register`, `POST /api/auth/login`, `GET /api/auth/google`, `GET/POST /api/folders`, `GET/POST /api/entries`, `POST /api/uploads`, `GET /api/skills`.

## Google login
The endpoint is an intentional configuration entry point. Add a Google OAuth client, set `GOOGLE_CLIENT_ID` and `GOOGLE_CLIENT_SECRET`, configure Spring Security OAuth2 login/callback, then issue this app's JWT after validating the Google identity. Do not ship client secrets or the development JWT secret.

## Interview code map
- REST + Spring MVC: `src/main/java/com/example/englishlog/entry/EntryController.java`
- JPA/entity/repository: `entry/Entry.java`, `entry/EntryRepository.java`
- DI/auth/password hashing/JWT: `auth/AuthController.java`, `config/SecurityConfig.java`, `config/JwtService.java`
- Multipart upload: `upload/UploadController.java`
- PostgreSQL/H2/config: `src/main/resources/application.yml`, `docker-compose.yml`
- Skills endpoint: `skill/SkillController.java`

## Production gaps to explain in an interview
Add JWT request filter and user-scoped authorization, Flyway migrations, object storage, image validation/scanning, refresh tokens, rate limiting, structured logs, metrics/tracing, tests/Testcontainers, and complete Google callback. These are deliberately listed as next issues rather than hidden.
