# Finance Metrics Backend - Agent Context

## Project Overview
REST API backend for tracking and calculating financial metrics (Debt/CF ratio, ETF flows, LME commodity inventory). Built with Kotlin/Ktor, PostgreSQL, containerized with Docker.

## Tech Stack Summary
- **Backend**: Ktor 2.3.7 (Kotlin framework)
- **Language**: Kotlin 1.9.22
- **Database**: PostgreSQL 16 with Exposed ORM 0.44.1
- **Migrations**: Flyway 9.22.3
- **Connection Pool**: HikariCP 5.1.0
- **DI**: Koin 3.5.3
- **Serialization**: kotlinx.serialization
- **Deployment**: Docker + Docker Compose
- **Testing**: Ktor Test Framework + JUnit 5
- **Auth** (Phase 2): Firebase Authentication

**Full details**: See `/docs/Tech_Stack.md`

## Development Environment
**Everything runs in Docker containers** - no JDK needed on host.

### Container Architecture
- `app` - Ktor backend (port 8080, debug 5005)
- `postgres` - Database (port 5432)
- `pgadmin` - DB GUI (port 5050 → http://localhost:5050)

### Common Commands
```bash
# Start all services (first time or after changes)
docker-compose up --build

# Start in background
docker-compose up -d --build

# View logs
docker-compose logs -f app
docker-compose logs -f postgres

# Run tests in container
docker-compose exec app ./gradlew test

# Restart app after code changes
docker-compose restart app

# Stop everything
docker-compose down

# Fresh start (wipe database)
docker-compose down -v && docker-compose up --build
```

## Project Structure
```
finance-metrics-backend/
├── .claude/
│   ├── AGENTS.md              # This file
│   └── GIT.md                 # Git workflow guidelines
├── docs/
│   ├── Tech_Stack.md          # Complete tech stack details
│   └── App_Context.md         # Application scope, metrics, schema
├── src/
│   ├── main/kotlin/com/financemetrics/
│   │   ├── Application.kt     # Entry point
│   │   ├── database/
│   │   │   ├── DatabaseFactory.kt  # HikariCP + Flyway setup
│   │   │   └── Tables.kt           # Exposed table definitions
│   │   ├── models/
│   │   │   └── Models.kt           # Request/Response DTOs
│   │   ├── plugins/
│   │   │   ├── HTTP.kt             # CORS, DefaultHeaders
│   │   │   ├── Monitoring.kt       # CallLogging
│   │   │   ├── Routing.kt          # Route configuration
│   │   │   ├── Security.kt         # RateLimit, StatusPages
│   │   │   └── Serialization.kt    # JSON ContentNegotiation
│   │   └── routes/
│   │       ├── CompaniesRoutes.kt
│   │       ├── EtfFlowsRoutes.kt
│   │       ├── HealthRoutes.kt
│   │       └── LmeInventoryRoutes.kt
│   ├── main/resources/
│   │   ├── application.yaml
│   │   ├── logback.xml
│   │   └── db/migration/
│   │       └── V1__create_initial_schema.sql
│   └── test/kotlin/com/financemetrics/
│       └── ApplicationTest.kt
├── build.gradle.kts
├── docker-compose.yml
├── Dockerfile
├── .gitignore
├── .env.example
└── README.md
```

## Application Phases

### Phase 1 - MVP (Current)
- Single user, no authentication
- Manual data entry for ETF flows and LME inventory
- Placeholder routes (repository logic TODO)
- Calculate Debt/CF ratio
- Basic CRUD operations

### Phase 2 - Multi-User (Future)
- Firebase Authentication
- User-specific watchlists
- Per-user preferences

### Phase 3 - Automation (Future)
- Automated LME data fetching
- Scheduled background jobs
- Data caching (Redis)

**Full scope**: See `/docs/App_Context.md`

## Core Metrics & Calculations

### 1. Debt to Cash Flow Ratio
```
Net Debt = Total Debt - Cash
Debt/CF Ratio = Net Debt ÷ TTM Operating Cash Flow
```
**Data source**: Manual entry (Phase 1), Financial APIs (future)

### 2. ETF Flow Trends
Compare 3-month vs 1-month inflow/outflow trends
**Data source**: Manual entry (monthly)

### 3. LME Inventory Levels
Track commodity inventory (Copper, Aluminum, etc.) over time
**Data source**: Manual entry (Phase 1), automated (Phase 3)

## Database Schema

### Tables
- `companies` - Company info (id UUID, ticker, name, sector, timestamps)
- `company_metrics` - Historical Debt/CF data (FK to companies)
- `etf_flows` - Monthly ETF flow data (unique: ticker + month)
- `lme_inventory` - Commodity inventory levels

**Full schema**: See `src/main/resources/db/migration/V1__create_initial_schema.sql`

## API Endpoints (Implemented)

### Health
- `GET /health` - Health check with database status

### Companies
- `POST /api/companies` - Add company to tracking
- `GET /api/companies` - List tracked companies
- `GET /api/companies/{ticker}` - Get company details
- `DELETE /api/companies/{ticker}` - Remove company
- `GET /api/companies/{ticker}/metrics` - Latest Debt/CF ratio
- `POST /api/companies/{ticker}/metrics` - Submit metrics
- `GET /api/companies/{ticker}/metrics/history` - Metrics history

### ETF Flows
- `POST /api/etf-flows` - Submit monthly flow data
- `GET /api/etf-flows` - List all ETF flows
- `GET /api/etf-flows/trends` - 3-month vs 1-month analysis
- `GET /api/etf-flows/{ticker}` - Get flows for specific ETF

### LME Inventory
- `POST /api/lme-inventory` - Submit inventory data
- `GET /api/lme-inventory` - List inventory entries
- `GET /api/lme-inventory/{commodity}` - Get commodity data
- `GET /api/lme-inventory/{commodity}/trends` - Trend analysis

**Full endpoint specs**: See `/docs/App_Context.md`

## Development Guidelines

### Code Style
- Follow Kotlin conventions
- Use data classes for DTOs/models
- Leverage Kotlin coroutines for async operations
- Use dependency injection (Koin) for testability
- Package: `com.financemetrics`

### Database Migrations
- **Never modify existing migrations**
- Create new migration file: `V{N}__description.sql` in `src/main/resources/db/migration/`
- Flyway runs migrations automatically on startup

### Testing
- Write tests for business logic (domain layer)
- API endpoint tests for routes
- Integration tests with TestContainers (future)
- Run tests: `docker-compose exec app ./gradlew test`

### Docker Development
- Code changes require rebuild: `docker-compose up --build`
- Database persists via Docker volume (survives container restarts)
- Use `docker-compose down -v` to wipe database

### Environment Variables
| Variable | Description | Default |
|----------|-------------|---------|
| DATABASE_URL | JDBC connection URL | jdbc:postgresql://postgres:5432/financemetrics |
| DATABASE_USER | Database username | postgres |
| DATABASE_PASSWORD | Database password | postgres |

## Debugging
- **Remote debug port**: 5005 (exposed in docker-compose)
- **IntelliJ**: Run → Edit Configurations → Remote JVM Debug → localhost:5005
- **Logs**: `docker-compose logs -f app`

## Common Issues & Solutions

### Container won't start
```bash
docker-compose logs app
docker-compose up --build
```

### Database connection errors
```bash
docker-compose ps
docker-compose logs postgres
```

### Code changes not reflecting
```bash
docker-compose up --build
```

## Learning Resources
- **Ktor**: https://ktor.io/docs/
- **Exposed ORM**: https://github.com/JetBrains/Exposed/wiki
- **Koin DI**: https://insert-koin.io/docs/reference/introduction
- **Docker**: https://docs.docker.com/

## Notes for AI Agents
- User prefers clean host machine (everything in containers)
- User has Android dev background (familiar with Kotlin, Gradle)
- Project uses container-first development approach
- Database migrations managed by Flyway (version-controlled schema changes)
- Single module architecture with clean package separation
- Authentication deferred to Phase 2 to simplify MVP
- **Project documentation** should be stored in the `docs/` folder
- Routes currently return placeholder responses - repository logic is TODO