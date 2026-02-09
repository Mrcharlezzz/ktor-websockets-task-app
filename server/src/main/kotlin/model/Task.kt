package com.example.model

import kotlinx.serialization.Serializable

/**
 * Priority levels for a `Task`.
 */
enum class Priority {
    Low, Medium, High, Vital
}

/**
 * A unit of work exchanged over HTTP/WS and stored in the DB.
 */
@Serializable
data class Task(
    val name: String,
    val description: String,
    val priority: Priority
)