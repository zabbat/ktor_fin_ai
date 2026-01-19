# Application Context

Detailed specification of the Finance Metrics Backend application scope, business logic, and data models.

## Application Overview

The Finance Metrics Backend is a REST API for tracking and analyzing financial indicators:

1. **Debt to Cash Flow Ratio** - Company financial health indicator
2. **ETF Flow Trends** - ETF inflow/outflow analysis
3. **LME Inventory Levels** - Commodity inventory tracking

## Development Phases

### Phase 1 - MVP (Current)

- Single user, no authentication
- Manual data entry for ETF flows and LME inventory
- API integration for company financials (future)
- Calculate Debt/CF ratio
- Basic CRUD operations
- Docker-based development

### Phase 2 - Multi-User

- Firebase Authentication integration
- User-specific watchlists
- Per-user preferences
- API key management

### Phase 3 - Automation

- Automated LME data fetching
- Scheduled background jobs
- Data caching (Redis)
- Webhook notifications

## Core Metrics & Calculations

### 1. Debt to Cash Flow Ratio

Measures how many years it would take a company to pay off its debt using operating cash flow.

**Formula:**
```
Net Debt = Total Debt - Cash and Cash Equivalents
Debt/CF Ratio = Net Debt ÷ TTM Operating Cash Flow
```

**Interpretation:**
- `< 1.0` - Excellent (can pay off debt in less than a year)
- `1.0 - 3.0` - Good
- `3.0 - 5.0` - Moderate concern
- `> 5.0` - High leverage risk

**Data Sources (Phase 1):**
- Manual entry via API
- Future: Alpha Vantage, Financial Modeling Prep, Polygon.io

### 2. ETF Flow Trends

Compare short-term vs medium-term flows to identify momentum.

**Analysis:**
- **1-Month Flow**: Most recent month's net flow
- **3-Month Flow**: Sum of last 3 months' flows
- **Trend**: Inflow (positive), Outflow (negative), Neutral

**Interpretation:**
- 3-month positive + 1-month positive = Strong inflow momentum
- 3-month positive + 1-month negative = Potential trend reversal
- 3-month negative + 1-month positive = Recovery signal

### 3. LME Inventory Levels

Track commodity warehouse inventory levels over time.

**Commodities Tracked:**
- Copper
- Aluminum
- Zinc
- Nickel
- Lead
- Tin

**Analysis:**
- Compare current vs previous levels
- Calculate percentage change
- Identify inventory build or draw

## Database Schema

### Entity Relationship Diagram

```
┌─────────────┐       ┌──────────────────┐
│  companies  │───1:N─│  company_metrics │
└─────────────┘       └──────────────────┘

┌─────────────┐       ┌──────────────────┐
│  etf_flows  │       │  lme_inventory   │
└─────────────┘       └──────────────────┘
```

### Table Definitions

#### companies

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, auto-gen | Unique identifier |
| ticker | VARCHAR(10) | UNIQUE, NOT NULL | Stock ticker symbol |
| name | VARCHAR(255) | NOT NULL | Company name |
| sector | VARCHAR(100) | NULLABLE | Industry sector |
| created_at | TIMESTAMP | DEFAULT NOW | Record creation time |
| updated_at | TIMESTAMP | DEFAULT NOW | Last update time |

#### company_metrics

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, auto-gen | Unique identifier |
| company_id | UUID | FK → companies.id | Related company |
| total_debt | DECIMAL(20,2) | NOT NULL | Total debt amount |
| cash | DECIMAL(20,2) | NOT NULL | Cash and equivalents |
| operating_cf | DECIMAL(20,2) | NOT NULL | TTM operating cash flow |
| debt_cf_ratio | DECIMAL(10,4) | NULLABLE | Calculated ratio |
| recorded_at | DATE | NOT NULL | Date of data |
| created_at | TIMESTAMP | DEFAULT NOW | Record creation time |

#### etf_flows

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, auto-gen | Unique identifier |
| etf_ticker | VARCHAR(10) | NOT NULL | ETF ticker symbol |
| flow_amount | DECIMAL(20,2) | NOT NULL | Net flow (+ inflow, - outflow) |
| month | DATE | NOT NULL | First day of month |
| created_at | TIMESTAMP | DEFAULT NOW | Record creation time |

**Unique Constraint:** (etf_ticker, month)

#### lme_inventory

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, auto-gen | Unique identifier |
| commodity | VARCHAR(50) | NOT NULL | Commodity name |
| inventory_tons | DECIMAL(20,2) | NOT NULL | Inventory in metric tons |
| recorded_at | DATE | NOT NULL | Date of recording |
| created_at | TIMESTAMP | DEFAULT NOW | Record creation time |

## API Specifications

### Request/Response Formats

All endpoints use JSON. Amounts are returned as strings to preserve precision.

#### Error Response Format

```json
{
  "error": "Not Found",
  "message": "Company with ticker XXXX not found",
  "status": 404
}
```

### Companies API

#### POST /api/companies

Create a new company to track.

**Request:**
```json
{
  "ticker": "AAPL",
  "name": "Apple Inc.",
  "sector": "Technology"
}
```

**Response (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "ticker": "AAPL",
  "name": "Apple Inc.",
  "sector": "Technology",
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T10:30:00Z"
}
```

#### GET /api/companies/{ticker}/metrics

Get latest metrics for a company.

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440001",
  "companyId": "550e8400-e29b-41d4-a716-446655440000",
  "ticker": "AAPL",
  "totalDebt": "109000000000.00",
  "cash": "62000000000.00",
  "netDebt": "47000000000.00",
  "operatingCf": "110000000000.00",
  "debtCfRatio": "0.4273",
  "recordedAt": "2024-01-15",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

### ETF Flows API

#### POST /api/etf-flows

Submit monthly ETF flow data.

**Request:**
```json
{
  "etfTicker": "SPY",
  "flowAmount": "5000000000.00",
  "month": "2024-01-01"
}
```

#### GET /api/etf-flows/trends?ticker=SPY

Get flow trend analysis.

**Response (200 OK):**
```json
{
  "etfTicker": "SPY",
  "oneMonthFlow": "5000000000.00",
  "threeMonthFlow": "12000000000.00",
  "trend": "inflow",
  "percentageChange": "25.5"
}
```

### LME Inventory API

#### GET /api/lme-inventory/{commodity}/trends

Get inventory trend analysis.

**Response (200 OK):**
```json
{
  "commodity": "COPPER",
  "currentInventory": "125000.00",
  "previousInventory": "130000.00",
  "change": "-5000.00",
  "percentageChange": "-3.85",
  "trend": "draw"
}
```

## Business Rules

### Validation Rules

1. **Ticker Format**: 1-10 uppercase alphanumeric characters
2. **Month Format**: First day of month (YYYY-MM-01)
3. **Amounts**: Decimal with 2 decimal places
4. **Dates**: ISO 8601 format

### Calculation Rules

1. **Net Debt**: Cannot be negative (min 0)
2. **Debt/CF Ratio**: NULL if operating CF is 0 or negative
3. **Trend Direction**:
   - `inflow` if flow > 0
   - `outflow` if flow < 0
   - `neutral` if flow = 0

### Data Constraints

1. No duplicate company tickers
2. No duplicate ETF flow entries (same ticker + month)
3. Metrics must reference existing company
4. Cascade delete metrics when company is removed

## Security Considerations

### Phase 1 (MVP)

- Rate limiting: 100 requests/minute global
- API rate limiting: 50 requests/minute
- SQL injection prevention via parameterized queries
- Input validation on all endpoints

### Phase 2+

- Firebase JWT token validation
- User-based access control
- API key authentication for integrations
- HTTPS enforcement

## External Integrations (Future)

### Financial Data APIs

| Provider | Free Tier Limit | Use Case |
|----------|-----------------|----------|
| Alpha Vantage | 25 calls/day | Company financials |
| Financial Modeling Prep | 250 calls/day | Financial statements |
| Polygon.io | 5 calls/minute | Market data |
| Yahoo Finance | Unofficial | Backup source |

### LME Data (Phase 3)

- Official LME API for warehouse inventory
- Scheduled daily data fetch
- Caching with Redis
