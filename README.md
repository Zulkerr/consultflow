# ConsultFlow — IT-Consulting Ressourcen- und Projektallokations-API

RESTful Backend-Service zur Verwaltung von IT-Beratern, Kundenprojekten und kapazitätsgeprüften Projektzuweisungen. Entwickelt für typische IT-Consulting-Prozesse mit strikter Auslastungsprüfung, Skill-Matching und standardisierten Fehlerantworten nach RFC 7807.

## Tech Stack
- **Sprache & Framework:** Java 17, Spring Boot 3.4+
- **Datenbank & Migrationen:** PostgreSQL 16 (Docker Compose), Flyway
- **Datenzugriff & Validierung:** Spring Data JPA, Hibernate (`ddl-auto: validate`), Jakarta Bean Validation
- **API & Fehlerbehandlung:** RESTful JSON-API mit unveränderlichen Java-`record`-DTOs, RFC 7807 `ProblemDetail` über `@RestControllerAdvice`
- **Tests:** JUnit 5, Mockito, AssertJ

## Architektur
Die Anwendung folgt einer strikten Schichtenarchitektur (`controller` -> `service` -> `repository` -> `entity`). JPA-Entities werden an den API-Schnittstellen niemals direkt nach außen gegeben; sämtliche Requests und Responses werden über unveränderliche Java-`record`-DTOs abgebildet.

### Zentrale Geschäftsregeln
- **Überbuchungsschutz:** Berechnet überlappende Projektzuweisungen per JPQL-Aggregation (`COALESCE(SUM(...), 0)`) und lehnt Buchungen, die die maximale Kapazität (`maxCapacityPercent`) eines Beraters überschreiten, mit `HTTP 409 Conflict` ab.
- **Skill-Matching:** Stellt sicher, dass der Haupt-Skill (`primarySkill`) des Beraters mit dem geforderten Projekt-Skill (`requiredSkill`) übereinstimmt (ohne Beachtung von Groß-/Kleinschreibung).
- **Projekt-Lebenszyklus:** Neue Projekte starten im Status `DRAFT`. Zuweisungen auf bereits abgeschlossene Projekte (`COMPLETED`) sowie ungültige Zeiträume (`endDate < startDate`) werden blockiert.
- **Eindeutige Berater:** Verhindert doppelte Registrierungen mit derselben E-Mail-Adresse (`HTTP 409 Conflict`).

## Schnellstart

1. **PostgreSQL starten:**
   `docker compose up -d`

2. **Unit-Tests ausführen:**
   `mvn test`

3. **Anwendung starten:**
   `mvn spring-boot:run`

Die API ist unter `http://localhost:8080` erreichbar. Flyway führt beim Start automatisch `V1__init_schema.sql` aus.

## API-Endpunkte

### Berater (`/api/v1/consultants`)
- `POST /api/v1/consultants` — Neuen Berater anlegen
- `GET /api/v1/consultants?skill=Java` — Alle Berater abrufen (optionaler Skill-Filter)
- `GET /api/v1/consultants/{id}` — Berater per ID abrufen

### Projekte (`/api/v1/projects`)
- `POST /api/v1/projects` — Neues Projekt anlegen (Status `DRAFT`)
- `GET /api/v1/projects?status=ACTIVE` — Alle Projekte abrufen (optionaler Status-Filter)
- `GET /api/v1/projects/{id}` — Projekt per ID abrufen
- `PATCH /api/v1/projects/{id}/status?status=ACTIVE` — Projektstatus ändern (`DRAFT` -> `ACTIVE` -> `COMPLETED`)

### Zuweisungen (`/api/v1/allocations`)
- `POST /api/v1/allocations` — Berater einem Projekt zuweisen
- `GET /api/v1/allocations/consultant/{consultantId}` — Alle Zuweisungen eines Beraters abrufen
- `GET /api/v1/allocations/project/{projectId}` — Alle Zuweisungen eines Projekts abrufen

## Beispiel-Requests

**1. Berater anlegen:**
`curl -X POST http://localhost:8080/api/v1/consultants -H "Content-Type: application/json" -d '{"name":"Anna Weber","email":"anna@consultflow.de","primarySkill":"Java","maxCapacityPercent":100}'`

**2. Projekt anlegen:**
`curl -X POST http://localhost:8080/api/v1/projects -H "Content-Type: application/json" -d '{"title":"Cloud Migration","clientName":"FinBank AG","requiredSkill":"Java"}'`

**3. Zuweisung erstellen (70 %):**
`curl -X POST http://localhost:8080/api/v1/allocations -H "Content-Type: application/json" -d '{"consultantId":1,"projectId":1,"allocationPercent":70,"startDate":"2026-12-01","endDate":"2026-12-31"}'`

Überschreitet eine weitere Buchung im überlappenden Zeitraum die verbleibenden `30 %` Restkapazität, antwortet die API mit einer RFC-7807-`ProblemDetail`-Meldung (`409 Conflict`).
