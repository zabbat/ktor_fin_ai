# Finance Metrics Backend - Agent Context

## Project Overview
REST API backend for tracking and calculating financial metrics (Debt/CF ratio, ETF flows, LME commodity inventory). Built with Kotlin/Ktor, PostgreSQL, containerized with Docker.

## Tech Stack Summary
- **Backend**: Ktor (Kotlin framework)
- **Language**: Kotlin
- **Database**: PostgreSQL with Exposed ORM
- **Deployment**: Docker + Docker Compose
- **DI**: Koin
- **Testing**: Ktor Test Framework + JUnit 5
- **CI/CD**: GitHub Actions
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
# Start all services
docker-compose up

# Start in background
docker-compose up -d

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
docker-compose down -v && docker-compose up
```

## Project Structure
```
finance-metrics-backend/
├── .claude/
│   └── AGENTS.md              # This file
├── docs/
│   ├── Tech_Stack.md          # Complete tech stack details
│   └── App_Context.md         # Application scope, metrics, schema
├── src/
│   ├── main/kotlin/
│   │   ├── Application.kt     # Entry point
│   │   ├── plugins/           # Ktor plugins (routing, serialization, etc.)
│   │   ├── routes/            # API endpoints
│   │   ├── models/            # Data classes/DTOs
│   │   ├── domain/            # Business logic
│   │   ├── database/          # DB setup, repositories
│   │   └── services/          # External API integrations
│   └── test/kotlin/           # Tests
├── src/main/resources/
│   └── db/migration/          # Flyway SQL migrations
├── docker-compose.yml         # Local dev environment
├── Dockerfile                 # Production container
├── build.gradle.kts           # Gradle build configuration
└── .env                       # Local environment variables (git-ignored)
```

## Application Phases

### Phase 1 - MVP (Current)
- Single user, no authentication
- Manual data entry for ETF flows and LME inventory
- API integration for company financials
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
**Data source**: Financial APIs (Alpha Vantage, FMP, etc.)

### 2. ETF Flow Trends
Compare 3-month vs 1-month inflow/outflow trends  
**Data source**: Manual entry (monthly)

### 3. LME Inventory Levels
Track commodity inventory (Copper, Gold, etc.) over time  
**Data source**: Manual entry (Phase 1), automated (Phase 3)

## Database Schema (Phase 1)

### Tables
- `companies` - Company info (ticker, name, sector)
- `company_metrics` - Historical Debt/CF data
- `etf_flows` - Monthly ETF flow data
- `lme_inventory` - Commodity inventory levels

**Full schema with SQL**: See `/docs/App_Context.md`

## API Endpoints (Planned)

### Companies
- `POST /api/companies` - Add company to tracking
- `GET /api/companies` - List tracked companies
- `GET /api/companies/{ticker}` - Get company details
- `DELETE /api/companies/{ticker}` - Remove company
- `GET /api/companies/{ticker}/metrics` - Latest Debt/CF ratio

### ETF Flows
- `POST /api/etf-flows` - Submit monthly flow data
- `GET /api/etf-flows` - List all ETF flows
- `GET /api/etf-flows/trends` - 3-month vs 1-month analysis

### LME Inventory
- `POST /api/lme-inventory` - Submit inventory data
- `GET /api/lme-inventory` - List inventory entries
- `GET /api/lme-inventory/{commodity}` - Get commodity data
- `GET /api/lme-inventory/{commodity}/trends` - Trend analysis

### Health
- `GET /health` - API health check
- `GET /api/docs` - Swagger UI

**Full endpoint specs**: See `/docs/App_Context.md`

## Development Guidelines

### Code Style
- Follow Kotlin conventions
- Use data classes for DTOs/models
- Leverage Kotlin coroutines for async operations
- Use dependency injection (Koin) for testability

### Database Migrations
- **Never modify existing migrations**
- Create new migration file: `VX__description.sql` in `src/main/resources/db/migration/`
- Flyway runs migrations automatically on startup

### Testing
- Write tests for business logic (domain layer)
- API endpoint tests for routes
- Integration tests for database operations
- Run tests: `docker-compose exec app ./gradlew test`

### Docker Development
- Code changes require container restart: `docker-compose restart app`
- For hot reload setup, we use volume mounting (see `docker-compose.yml`)
- Database persists via Docker volume (survives container restarts)

### Environment Variables
- Local: Use `.env` file (git-ignored)
- Production: Environment variables in Docker/VM
- Never commit secrets to Git

## External Integrations

### Financial APIs (Phase 1)
- **Alpha Vantage**: 25 calls/day (free tier)
- **Financial Modeling Prep**: 250 calls/day (free tier)
- **Polygon.io**: 5 calls/minute (free tier)
- **Yahoo Finance**: Unofficial libraries

### Future Integrations (Phase 3)
- LME API for automated inventory data
- Scheduled jobs for data fetching

## Security Considerations
- HTTPS enforced in production (Nginx + Let's Encrypt)
- Rate limiting on API endpoints
- CORS configured for allowed origins
- Parameterized queries (SQL injection prevention)
- Firebase token validation (Phase 2)

## Debugging
- **Remote debug port**: 5005 (exposed in docker-compose)
- **IntelliJ**: Run → Attach to Process → localhost:5005
- **Logs**: `docker-compose logs -f app`

## Common Issues & Solutions

### Container won't start
```bash
# Check logs
docker-compose logs app

# Rebuild container
docker-compose build app
docker-compose up
```

### Database connection errors
```bash
# Verify postgres is running
docker-compose ps

# Check postgres logs
docker-compose logs postgres

# Ensure app waits for postgres (depends_on in docker-compose.yml)
```

### Code changes not reflecting
```bash
# Restart app container
docker-compose restart app

# Or rebuild if Dockerfile/dependencies changed
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
- Multi-module architecture discussion: decided to start with single module, clean packages
- Authentication deferred to Phase 2 to simplify MVP
- **Project documentation** should be stored in the `docs/` folder