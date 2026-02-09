package com.example

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.application.install
import io.ktor.server.http.content.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.model.Task
import com.example.model.TaskRepository

/**
 * Configures plain HTTP routing.
 *
 * Endpoints:
 * - GET `/` → returns "Healthy" (health check)
 * - GET `/tasks` → returns all tasks as JSON
 * - POST `/tasks` → accepts a `Task` JSON, stores it, and returns the created task
 * - Static resources under `/static` → serves files from `resources/static`.
 */
fun Application.configureRouting(taskRepository: TaskRepository) {
    routing {
        install(ContentNegotiation) {
            json()
        }
        get("/") {
            call.respondText("Healthy")
        }

        // HTTP endpoints mirroring WS functionality: list and create tasks
        route("/tasks") {
            get {
                val tasks = taskRepository.allTasks()
                call.respond(tasks)
            }
            post {
                val task = call.receive<Task>()
                taskRepository.addTask(task)
                call.respond(task)
            }
        }

        // Static plugin. Try Access `/static/wsClient.html`
        staticResources("/static", "static")
    }
}
