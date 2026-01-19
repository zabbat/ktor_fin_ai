package com.financemetrics.models

import kotlinx.serialization.Serializable

// ============== Company Models ==============

@Serializable
data class CompanyRequest(
    val ticker: String,
    val name: String,
    val sector: String? = null
)

@Serializable
data class CompanyResponse(
    val id: String,
    val ticker: String,
    val name: String,
    val sector: String?,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class CompanyListResponse(
    val companies: List<CompanyResponse>,
    val total: Int
)

// ============== Company Metrics Models ==============

@Serializable
data class CompanyMetricsRequest(
    val totalDebt: String,
    val cash: String,
    val operatingCf: String,
    val recordedAt: String
)

@Serializable
data class CompanyMetricsResponse(
    val id: String,
    val companyId: String,
    val ticker: String,
    val totalDebt: String,
    val cash: String,
    val netDebt: String,
    val operatingCf: String,
    val debtCfRatio: String?,
    val recordedAt: String,
    val createdAt: String
)

@Serializable
data class MetricsHistoryResponse(
    val ticker: String,
    val metrics: List<CompanyMetricsResponse>,
    val total: Int
)

// ============== ETF Flow Models ==============

@Serializable
data class EtfFlowRequest(
    val etfTicker: String,
    val flowAmount: String,
    val month: String
)

@Serializable
data class EtfFlowResponse(
    val id: String,
    val etfTicker: String,
    val flowAmount: String,
    val month: String,
    val createdAt: String
)

@Serializable
data class EtfFlowListResponse(
    val flows: List<EtfFlowResponse>,
    val total: Int
)

@Serializable
data class EtfTrendResponse(
    val etfTicker: String,
    val oneMonthFlow: String,
    val threeMonthFlow: String,
    val trend: String,
    val percentageChange: String?
)

// ============== LME Inventory Models ==============

@Serializable
data class LmeInventoryRequest(
    val commodity: String,
    val inventoryTons: String,
    val recordedAt: String
)

@Serializable
data class LmeInventoryResponse(
    val id: String,
    val commodity: String,
    val inventoryTons: String,
    val recordedAt: String,
    val createdAt: String
)

@Serializable
data class LmeInventoryListResponse(
    val inventory: List<LmeInventoryResponse>,
    val total: Int
)

@Serializable
data class LmeTrendResponse(
    val commodity: String,
    val currentInventory: String,
    val previousInventory: String,
    val change: String,
    val percentageChange: String,
    val trend: String
)

// ============== Common Models ==============

@Serializable
data class HealthResponse(
    val status: String,
    val database: String,
    val timestamp: String
)

@Serializable
data class ErrorResponse(
    val error: String,
    val message: String,
    val status: Int
)

@Serializable
data class MessageResponse(
    val message: String
)
