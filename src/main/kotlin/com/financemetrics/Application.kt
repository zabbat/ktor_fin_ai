package com.financemetrics

import com.financemetrics.database.DatabaseFactory
import com.financemetrics.plugins.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.inject

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    // Initialize database
    DatabaseFactory.init(environment.config)

    // Configure plugins
    configureSerialization()
    configureHTTP()
    configureMonitoring()
    configureSecurity()
    configureRouting()
}