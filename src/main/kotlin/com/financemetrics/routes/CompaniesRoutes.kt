package com.financemetrics.routes

import com.financemetrics.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.companiesRoutes() {
    route("/api/companies") {
        // POST /api/companies - Add a new company
        post {
            val request = call.receive<CompanyRequest>()

            // TODO: Implement repository logic to save company
            // - Validate ticker format
            // - Check for duplicates
            // - Save to database
            // - Return created company

            call.respond(
                HttpStatusCode.Created,
                CompanyResponse(
                    id = "placeholder-uuid",
                    ticker = request.ticker.uppercase(),
                    name = request.name,
                    sector = request.sector,
                    createdAt = "2024-01-01T00:00:00Z",
                    updatedAt = "2024-01-01T00:00:00Z"
                )
            )
        }

        // GET /api/companies - List all companies
        get {
            // TODO: Implement repository logic to fetch all companies
            // - Support pagination (offset, limit query params)
            // - Support filtering by sector

            call.respond(
                HttpStatusCode.OK,
                CompanyListResponse(
                    companies = emptyList(),
                    total = 0
                )
            )
        }

        // GET /api/companies/{ticker} - Get company by ticker
        get("/{ticker}") {
            val ticker = call.parameters["ticker"]
                ?: throw IllegalArgumentException("Ticker is required")

            // TODO: Implement repository logic to fetch company by ticker
            // - Return 404 if not found

            call.respond(
                HttpStatusCode.OK,
                CompanyResponse(
                    id = "placeholder-uuid",
                    ticker = ticker.uppercase(),
                    name = "Placeholder Company",
                    sector = null,
                    createdAt = "2024-01-01T00:00:00Z",
                    updatedAt = "2024-01-01T00:00:00Z"
                )
            )
        }

        // DELETE /api/companies/{ticker} - Remove company
        delete("/{ticker}") {
            val ticker = call.parameters["ticker"]
                ?: throw IllegalArgumentException("Ticker is required")

            // TODO: Implement repository logic to delete company
            // - Also delete related metrics
            // - Return 404 if not found

            call.respond(
                HttpStatusCode.OK,
                MessageResponse(message = "Company $ticker deleted successfully")
            )
        }

        // GET /api/companies/{ticker}/metrics - Get latest metrics
        get("/{ticker}/metrics") {
            val ticker = call.parameters["ticker"]
                ?: throw IllegalArgumentException("Ticker is required")

            // TODO: Implement repository logic to fetch latest metrics
            // - Calculate net debt and debt/cf ratio
            // - Return 404 if company or metrics not found

            call.respond(
                HttpStatusCode.OK,
                CompanyMetricsResponse(
                    id = "placeholder-uuid",
                    companyId = "placeholder-company-uuid",
                    ticker = ticker.uppercase(),
                    totalDebt = "0.00",
                    cash = "0.00",
                    netDebt = "0.00",
                    operatingCf = "0.00",
                    debtCfRatio = null,
                    recordedAt = "2024-01-01",
                    createdAt = "2024-01-01T00:00:00Z"
                )
            )
        }

        // POST /api/companies/{ticker}/metrics - Add metrics
        post("/{ticker}/metrics") {
            val ticker = call.parameters["ticker"]
                ?: throw IllegalArgumentException("Ticker is required")
            val request = call.receive<CompanyMetricsRequest>()

            // TODO: Implement repository logic to save metrics
            // - Validate company exists
            // - Calculate net debt and debt/cf ratio
            // - Save to database

            call.respond(
                HttpStatusCode.Created,
                CompanyMetricsResponse(
                    id = "placeholder-uuid",
                    companyId = "placeholder-company-uuid",
                    ticker = ticker.uppercase(),
                    totalDebt = request.totalDebt,
                    cash = request.cash,
                    netDebt = "0.00",
                    operatingCf = request.operatingCf,
                    debtCfRatio = null,
                    recordedAt = request.recordedAt,
                    createdAt = "2024-01-01T00:00:00Z"
                )
            )
        }

        // GET /api/companies/{ticker}/metrics/history - Get metrics history
        get("/{ticker}/metrics/history") {
            val ticker = call.parameters["ticker"]
                ?: throw IllegalArgumentException("Ticker is required")

            // TODO: Implement repository logic to fetch metrics history
            // - Support date range filtering (from, to query params)
            // - Order by recorded_at descending

            call.respond(
                HttpStatusCode.OK,
                MetricsHistoryResponse(
                    ticker = ticker.uppercase(),
                    metrics = emptyList(),
                    total = 0
                )
            )
        }
    }
}
