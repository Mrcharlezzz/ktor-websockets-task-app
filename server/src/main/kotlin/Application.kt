package com.example

import com.example.model.SqliteTaskRepository
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    val repository = SqliteTaskRepository()
    configureDatabases()
    configureSockets(repository)
    configureRouting(repository)
}
