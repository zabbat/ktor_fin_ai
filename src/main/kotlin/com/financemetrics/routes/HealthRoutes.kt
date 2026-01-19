package com.financemetrics.routes

import com.financemetrics.models.HealthResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

fun Route.healthRoutes() {
    get("/health") {
        val dbStatus = try {
            transaction {
                exec("SELECT 1")
            }
            "connected"
        } catch (e: Exception) {
            "disconnected"
        }

        val status = if (dbStatus == "connected") "healthy" else "unhealthy"
        val statusCode = if (dbStatus == "connected") HttpStatusCode.OK else HttpStatusCode.ServiceUnavailable

        call.respond(
            statusCode,
            HealthResponse(
                status = status,
                database = dbStatus,
                timestamp = Instant.now().toString()
            )
        )
    }
}
