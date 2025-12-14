package com.example

import com.example.client.HttpClientProvider
import kotlinx.coroutines.runBlocking
import model.Priority
import model.Task

fun main() = runBlocking {
    val http = HttpClientProvider.create()
    val api = TaskServerClient(http, baseUrl = "http://localhost:8080")

    println("Health: ${api.health()}")

    val task = Task(
        name = "Test task",
        description = "Created from client",
        priority = Priority.High
    )

    val created = api.createTask(task)
    println("Created task: $created")

    val allTasks = api.listTasks()
    println("All tasks:")
    allTasks.forEach { println(it) }

    http.close()
}
