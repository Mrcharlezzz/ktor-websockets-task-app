package com.example.model

/**
 * Abstraction over task persistence.
 */
interface TaskRepository{
    /** Returns all tasks. */
    suspend fun allTasks(): List<Task>
    /** Returns tasks that match the given [priority]. */
    suspend fun tasksByPriority(priority: Priority): List<Task>
    /** Returns the task with the given [name], or null if not found. */
    suspend fun taskByName(name: String): Task?
    /** Persists a new [task]. */
    suspend fun addTask(task: Task)
    /** Deletes the task with [name]; returns true if a row was removed. */
    suspend fun removeTask(name: String): Boolean
}