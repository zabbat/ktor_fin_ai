# Finance Metrics Backend

REST API backend for tracking and calculating financial metrics including Debt/CF ratio, ETF flows, and LME commodity inventory.

## Tech Stack

- **Backend**: Kotlin + Ktor 2.3.7
- **Database**: PostgreSQL 16 + Exposed ORM
- **Migrations**: Flyway
- **DI**: Koin
- **Deployment**: Docker + Docker Compose

## Quick Start

### Prerequisites

- Docker and Docker Compose installed
- No JDK required on host (runs in containers)

### Start the Application

```bash
# Start all services (app, postgres, pgadmin)
docker-compose up --build

# Or run in background
docker-compose up -d --build
```

The API will be available at `http://localhost:8080`

### Access Services

| Service | URL | Credentials |
|---------|-----|-------------|
| API | http://localhost:8080 | - |
| pgAdmin | http://localhost:5050 | admin@financemetrics.com / admin |
| PostgreSQL | localhost:5432 | postgres / postgres |

### View Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f app
docker-compose logs -f postgres
```

### Stop Services

```bash
# Stop containers
docker-compose down

# Stop and remove volumes (wipes database)
docker-compose down -v
```

## API Endpoints

### Health

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/health` | Health check with database status |

### Companies

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/companies` | Add a company to tracking |
| GET | `/api/companies` | List all tracked companies |
| GET | `/api/companies/{ticker}` | Get company details |
| DELETE | `/api/companies/{ticker}` | Remove company from tracking |
| GET | `/api/companies/{ticker}/metrics` | Get latest Debt/CF metrics |
| POST | `/api/companies/{ticker}/metrics` | Submit company metrics |
| GET | `/api/companies/{ticker}/metrics/history` | Get metrics history |

### ETF Flows

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/etf-flows` | Submit monthly ETF flow data |
| GET | `/api/etf-flows` | List all ETF flows |
| GET | `/api/etf-flows/trends` | Get 1-month vs 3-month trends |
| GET | `/api/etf-flows/{ticker}` | Get flows for specific ETF |

### LME Inventory

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/lme-inventory` | Submit inventory data |
| GET | `/api/lme-inventory` | List all inventory entries |
| GET | `/api/lme-inventory/{commodity}` | Get commodity inventory |
| GET | `/api/lme-inventory/{commodity}/trends` | Get commodity trends |

## Development

### Project Structure

```
finance-metrics-backend/
├── .claude/                    # AI agent context
├── docs/                       # Documentation
├── src/
│   ├── main/
│   │   ├── kotlin/com/financemetrics/
│   │   │   ├── Application.kt
│   │   │   ├── database/       # DB factory, tables
│   │   │   ├── models/         # DTOs
│   │   │   ├── plugins/        # Ktor plugins
│   │   │   └── routes/         # API routes
│   │   └── resources/
│   │       ├── application.yaml
│   │       ├── logback.xml
│   │       └── db/migration/   # Flyway migrations
│   └── test/
├── build.gradle.kts
├── docker-compose.yml
├── Dockerfile
└── README.md
```

### Running Tests

```bash
# Run tests in container
docker-compose exec app ./gradlew test
```

### Database Migrations

Migrations are run automatically on startup via Flyway. To add a new migration:

1. Create a new file: `src/main/resources/db/migration/V{N}__description.sql`
2. Restart the application

### Remote Debugging

The app exposes port 5005 for remote debugging. In IntelliJ:

1. Run → Edit Configurations → + → Remote JVM Debug
2. Host: localhost, Port: 5005
3. Start debugging

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| DATABASE_URL | JDBC connection URL | jdbc:postgresql://postgres:5432/financemetrics |
| DATABASE_USER | Database username | postgres |
| DATABASE_PASSWORD | Database password | postgres |

## License

MIT
