package com.financemetrics.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.slf4j.LoggerFactory

object DatabaseFactory {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun init(config: ApplicationConfig) {
        val databaseUrl = System.getenv("DATABASE_URL")
            ?: config.propertyOrNull("database.url")?.getString()
            ?: "jdbc:postgresql://localhost:5432/financemetrics"

        val databaseUser = System.getenv("DATABASE_USER")
            ?: config.propertyOrNull("database.user")?.getString()
            ?: "postgres"

        val databasePassword = System.getenv("DATABASE_PASSWORD")
            ?: config.propertyOrNull("database.password")?.getString()
            ?: "postgres"

        logger.info("Connecting to database: $databaseUrl")

        val dataSource = createHikariDataSource(databaseUrl, databaseUser, databasePassword)

        // Run Flyway migrations
        runMigrations(dataSource)

        // Connect Exposed to the database
        Database.connect(dataSource)

        logger.info("Database connection established successfully")
    }

    private fun createHikariDataSource(url: String, user: String, password: String): HikariDataSource {
        val config = HikariConfig().apply {
            jdbcUrl = url
            username = user
            this.password = password
            driverClassName = "org.postgresql.Driver"
            maximumPoolSize = 10
            minimumIdle = 2
            idleTimeout = 30000
            connectionTimeout = 30000
            maxLifetime = 1800000
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        return HikariDataSource(config)
    }

    private fun runMigrations(dataSource: HikariDataSource) {
        logger.info("Running database migrations...")
        val flyway = Flyway.configure()
            .dataSource(dataSource)
            .locations("classpath:db/migration")
            .baselineOnMigrate(true)
            .load()

        val result = flyway.migrate()
        logger.info("Applied ${result.migrationsExecuted} migrations")
    }
}
