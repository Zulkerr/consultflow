# Project: ConsultFlow (IT-Consulting Resource Allocation API)

## Tech Stack & Constraints
- Java 17, Spring Boot 3.4+, Maven 3.9+
- Dependencies: Spring Web, Spring Data JPA, PostgreSQL, Driver, Flayway, Bean Validation, Lombok, Spring Boot Test (JUnit 5 + Mockito)
- DB: PostgreSQL 16 (via Docker Compose)
- Hardware Constraint: 8 GB RAM - keep Docker setup Lightweight (Postgres only, no heavy UI containers).

## Architecture Rules
- Strict layered architecture: `controller` -> `service` -> `repository` -> `entity`
- Never expose JPA entities in REST endpoints; always use Java `record` DTOs (`dto/request`, `dto/response`).
- Centralize error handling via `@RestControllerAdvice` returning RFC 7807 `ProblemDetail`.
- Schema changes exclusively via Flyway SQL migrations (`src/main/resources/db/migration/V{n}__{desc}.sql`).

## Workflow Rules (STRICT)
- Modify/create at most 1-2 files per prompt.
- Write clean, concise code without unnecessary boilerplate or long comments.
- Run `mvn test` after logic changes to verify.
