package com.financemetrics.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime

object Companies : Table("companies") {
    val id = uuid("id").autoGenerate()
    val ticker = varchar("ticker", 10).uniqueIndex()
    val name = varchar("name", 255)
    val sector = varchar("sector", 100).nullable()
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id)
}

object CompanyMetrics : Table("company_metrics") {
    val id = uuid("id").autoGenerate()
    val companyId = uuid("company_id").references(Companies.id)
    val totalDebt = decimal("total_debt", 20, 2)
    val cash = decimal("cash", 20, 2)
    val operatingCf = decimal("operating_cf", 20, 2)
    val debtCfRatio = decimal("debt_cf_ratio", 10, 4).nullable()
    val recordedAt = date("recorded_at")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id)
}

object EtfFlows : Table("etf_flows") {
    val id = uuid("id").autoGenerate()
    val etfTicker = varchar("etf_ticker", 10)
    val flowAmount = decimal("flow_amount", 20, 2)
    val month = date("month")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id)
}

object LmeInventory : Table("lme_inventory") {
    val id = uuid("id").autoGenerate()
    val commodity = varchar("commodity", 50)
    val inventoryTons = decimal("inventory_tons", 20, 2)
    val recordedAt = date("recorded_at")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id)
}
