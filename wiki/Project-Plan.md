# Project Plan — RetourMarche

## Overview

**RetourMarche** is a full-stack e-commerce application dedicated to buying and selling refurbished smartphones and accessories. The project is built with **Spring Boot 3**, **Thymeleaf**, **Bootstrap 5**, **H2 database**, and **Spring Security**.

The application targets environmentally-conscious consumers who want quality refurbished mobile devices at a lower cost.

---

## Main Features to Implement / Improve

### Core E-Commerce
- Product catalogue (Smartphones & Accessories) with search and category filtering
- Shopping cart and order management
- Promo code system
- Product insurance options

### Security & Threat Preparedness
- Implement Spring Security (form login, CSRF protection, session management)
- Role-based access control (ADMIN / USER)
- Input validation and sanitization to prevent SQL injection and XSS
- HTTPS enforcement in production

### Authentication & Authorization
- Replace manual password hashing with Spring Security's `BCryptPasswordEncoder`
- JWT-based stateless sessions or Spring Session management
- OAuth2 / social login (future)

### Observability & Monitoring
- Structured JSON logging with **Logback** (Spring Boot's default logger)
- Expose metrics via **Spring Boot Actuator** + **Micrometer**
- Route metrics to **Prometheus** and visualize with **Grafana**
- Log aggregation with **Dozzle** (Docker-based log viewer)
- Export logs to a file or external system (ELK or Loki)

### Cloud Deployment & Infrastructure
- Containerize the application with **Docker**
- Orchestrate services with **Docker Compose**
- Deploy to **Render.com** (selected platform — see Deployment section)
- Configure an **API Gateway** (Nginx reverse proxy) as entry point

### Containerization & Scalability
- Multi-container Docker Compose setup: App + Nginx + Prometheus + Grafana
- Replace H2 with **PostgreSQL** for production persistence
- Health checks and restart policies in Docker Compose

### Architecture & Fault Tolerance
- Circuit breaker pattern for external calls (Resilience4j)
- Graceful error pages (404, 500)
- Transactional integrity for order/cart operations

### Performance Improvements
- Pagination on product listing pages
- Database query optimization and indexing
- Static asset caching (Nginx)

### Payment
- Integrate a payment gateway (Stripe or equivalent sandbox)
- Secure payment flow with server-side validation

### Database Review
- Migrate from **H2** (in-memory/file) to **PostgreSQL**
- Review entity relationships and add proper indexes
- Introduce Liquibase or Flyway for database migrations

---

## Deployment Platform

### Selected Platform: **Render.com**

Render was selected because:
- **Free tier** available for web services and databases
- Supports **Docker** and **Docker Compose** deployments natively
- Provides built-in **log streaming** (accessible from the dashboard and exportable)
- Exposes metrics compatible with external tools (Prometheus via custom endpoint using Spring Actuator)
- Supports environment variables and secret management
- **Gateway**: Nginx is included as a reverse proxy within the Docker Compose stack

### Deployment Architecture

```
Internet
    │
    ▼
[ Nginx (Gateway) ] ← port 80/443
    │
    ▼
[ Spring Boot App ] ← port 8080
    │
    ├──► [ PostgreSQL ] ← internal
    ├──► [ Prometheus ] ← /actuator/prometheus
    └──► [ Grafana ] ← port 3000
```

All containers are managed via `docker-compose.yml`. Logs from all containers are accessible in Render's dashboard and can also be viewed locally via **Dozzle**.

---

## Iteration Plan

### Iteration 1 — 9–16 Mar

| Field | Details |
|---|---|
| **Goal** | Stabilize the existing codebase, fix known bugs, and set up the development environment with Docker |
| **Major Work** | Fix Java compatibility issues (switch expressions), review the MVC structure, write `Dockerfile` and initial `docker-compose.yml`, replace H2 config for production |
| **Milestone** | Application runs successfully inside a Docker container locally; Docker Compose file starts the app + Nginx reverse proxy |

### Iteration 2 — 17–23 Mar

| Field | Details |
|---|---|
| **Goal** | Implement Spring Security, structured logging, and observability tools |
| **Major Work** | Add Spring Security (BCrypt, CSRF, roles), configure Logback JSON output, add Spring Boot Actuator + Micrometer, add Prometheus & Grafana services to Docker Compose, add Dozzle for live log viewing |
| **Milestone** | Users can log in securely (password hashed); application emits structured JSON logs visible in Dozzle; Grafana dashboard shows JVM and HTTP metrics |

### Iteration 3 — 24–30 Mar

| Field | Details |
|---|---|
| **Goal** | Migrate to PostgreSQL, improve architecture, and prepare for cloud deployment |
| **Major Work** | Replace H2 with PostgreSQL, add Flyway migrations, optimize queries, add circuit breaker, implement graceful error pages, deploy to Render.com |
| **Milestone** | Application deployed and publicly accessible on Render.com with PostgreSQL; structured logs accessible from Render dashboard |

### Iteration 4 — 31 Mar – 6 Apr

| Field | Details |
|---|---|
| **Goal** | Payment integration and final polishing |
| **Major Work** | Integrate Stripe payment sandbox, complete input validation, review security hardening, write documentation |
| **Milestone** | End-to-end purchase flow works including mock payment; all known security issues resolved; Wiki fully documented |

---

*Back to [Home](Home)*
