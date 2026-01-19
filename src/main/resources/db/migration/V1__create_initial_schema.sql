-- V1__create_initial_schema.sql
-- Initial database schema for Finance Metrics Backend

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Companies table
CREATE TABLE companies (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    ticker VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    sector VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create index on ticker for fast lookups
CREATE INDEX idx_companies_ticker ON companies(ticker);

-- Company metrics table
CREATE TABLE company_metrics (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    company_id UUID NOT NULL REFERENCES companies(id) ON DELETE CASCADE,
    total_debt DECIMAL(20, 2) NOT NULL,
    cash DECIMAL(20, 2) NOT NULL,
    operating_cf DECIMAL(20, 2) NOT NULL,
    debt_cf_ratio DECIMAL(10, 4),
    recorded_at DATE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for company_metrics
CREATE INDEX idx_company_metrics_company_id ON company_metrics(company_id);
CREATE INDEX idx_company_metrics_recorded_at ON company_metrics(recorded_at);

-- ETF flows table
CREATE TABLE etf_flows (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    etf_ticker VARCHAR(10) NOT NULL,
    flow_amount DECIMAL(20, 2) NOT NULL,
    month DATE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for etf_flows
CREATE INDEX idx_etf_flows_ticker ON etf_flows(etf_ticker);
CREATE INDEX idx_etf_flows_month ON etf_flows(month);

-- Unique constraint to prevent duplicate entries for same ETF and month
CREATE UNIQUE INDEX idx_etf_flows_ticker_month ON etf_flows(etf_ticker, month);

-- LME inventory table
CREATE TABLE lme_inventory (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    commodity VARCHAR(50) NOT NULL,
    inventory_tons DECIMAL(20, 2) NOT NULL,
    recorded_at DATE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for lme_inventory
CREATE INDEX idx_lme_inventory_commodity ON lme_inventory(commodity);
CREATE INDEX idx_lme_inventory_recorded_at ON lme_inventory(recorded_at);

-- Function to automatically update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Trigger to update updated_at on companies table
CREATE TRIGGER update_companies_updated_at
    BEFORE UPDATE ON companies
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();
