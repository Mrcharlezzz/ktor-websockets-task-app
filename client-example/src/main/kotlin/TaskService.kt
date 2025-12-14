package com.example


import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import model.Task

class TaskServerClient(
    private val http: HttpClient,
    private val baseUrl: String,
) {

    suspend fun health(): String =
        http.get("$baseUrl/").body()

    suspend fun createTask(task: Task): Task =
        http.post("$baseUrl/tasks") {
            contentType(ContentType.Application.Json)
            setBody(task)
        }.body()

    suspend fun listTasks(): List<Task> =
        http.get("$baseUrl/tasks").body()
}