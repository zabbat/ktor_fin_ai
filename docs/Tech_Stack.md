# Tech Stack

Detailed overview of the technology choices for the Finance Metrics Backend.

## Core Technologies

### Kotlin 1.9.22

**Why Kotlin?**
- Modern, concise syntax with null safety
- Excellent coroutine support for async operations
- Full Java interoperability
- Strong IDE support (IntelliJ IDEA)

### Ktor 2.3.7

**Why Ktor?**
- Lightweight, modular framework
- Native Kotlin coroutine support
- Plugin-based architecture
- Easy to test and maintain
- Built by JetBrains

**Plugins Used:**
- `ContentNegotiation` - JSON serialization
- `CORS` - Cross-origin resource sharing
- `CallLogging` - Request/response logging
- `StatusPages` - Error handling
- `RateLimit` - API rate limiting
- `DefaultHeaders` - Custom response headers

### PostgreSQL 16

**Why PostgreSQL?**
- Robust, battle-tested RDBMS
- Excellent JSON support
- UUID native type
- Advanced indexing capabilities
- Strong community and documentation

### Exposed ORM 0.44.1

**Why Exposed?**
- Kotlin-native ORM by JetBrains
- Type-safe SQL DSL
- Lightweight alternative to Hibernate
- Native coroutine support
- Easy to understand and debug

**Features Used:**
- Table definitions with type-safe columns
- UUID auto-generation
- Java Time support
- Transaction management

### Flyway 9.22.3

**Why Flyway?**
- Version-controlled migrations
- Automatic migration on startup
- Repeatable migrations support
- Easy rollback capabilities
- Team collaboration friendly

### HikariCP 5.1.0

**Why HikariCP?**
- Fastest JDBC connection pool
- Low latency, high performance
- Minimal configuration
- Battle-tested in production

**Configuration:**
- Max pool size: 10
- Min idle: 2
- Connection timeout: 30s
- Idle timeout: 30s

### Koin 3.5.3

**Why Koin?**
- Lightweight dependency injection
- Pure Kotlin DSL
- No annotation processing
- Easy testing
- Ktor integration

### kotlinx.serialization

**Why kotlinx.serialization?**
- Kotlin-native serialization
- Compile-time safety
- Fast performance
- No reflection overhead
- Works with multiplatform

## Infrastructure

### Docker

**Multi-stage Build:**
1. **Build stage**: Gradle 8.5 + JDK 17
2. **Runtime stage**: Eclipse Temurin JRE 17 Alpine

**Benefits:**
- Smaller final image (~200MB)
- No build tools in production
- Consistent environments
- Easy deployment

### Docker Compose

**Services:**
- `app` - Ktor backend (port 8080, debug 5005)
- `postgres` - PostgreSQL database (port 5432)
- `pgadmin` - Database GUI (port 5050)

**Features:**
- Health checks for dependencies
- Named volumes for persistence
- Custom network for isolation

## Testing

### Ktor Test Framework

- In-memory test server
- No network overhead
- Fast execution
- Full plugin support

### JUnit 5

- Modern testing framework
- Parallel test execution
- Parameterized tests
- Better assertions

### Future: TestContainers

- Real PostgreSQL in tests
- Docker-based integration tests
- Isolated test environments

## Security Considerations

### Current (Phase 1 - MVP)

- Rate limiting (100 req/min global, 50 req/min per API)
- CORS configuration
- SQL injection prevention (parameterized queries)
- Input validation

### Future (Phase 2+)

- Firebase Authentication
- JWT token validation
- HTTPS enforcement
- API key management

## Monitoring & Logging

### Logback 1.4.14

- Structured logging
- Log levels per package
- Console appender (Docker-friendly)
- Easy to add file/remote appenders

### Call Logging

- Request method and path
- Response status
- Processing time (ms)

## Performance Optimizations

### Connection Pooling

HikariCP maintains a pool of database connections:
- Reduces connection overhead
- Handles connection lifecycle
- Automatic connection validation

### Lazy Loading

Exposed ORM supports lazy loading for related entities to prevent N+1 queries.

### Indexing Strategy

Database indexes on:
- `companies.ticker` - Primary lookup field
- `company_metrics.company_id` - Foreign key joins
- `company_metrics.recorded_at` - Date range queries
- `etf_flows.etf_ticker` + `month` - Composite unique
- `lme_inventory.commodity` - Filtering

## Development Experience

### Hot Reload

Ktor supports development mode with automatic recompilation:
```yaml
ktor:
  deployment:
    watch:
      - classes
      - resources
```

### Remote Debugging

Debug port 5005 exposed in Docker Compose for IDE attachment.

### Database GUI

pgAdmin available at http://localhost:5050 for database inspection.
