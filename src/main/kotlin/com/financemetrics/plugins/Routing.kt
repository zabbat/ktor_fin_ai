package com.financemetrics.plugins

import com.financemetrics.routes.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        healthRoutes()
        companiesRoutes()
        etfFlowsRoutes()
        lmeInventoryRoutes()
    }
}
