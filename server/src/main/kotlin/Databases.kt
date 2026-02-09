package com.example

import com.example.db.TaskDAO
import com.example.db.TaskTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Configures the SQLite database and ensures the schema exists.
 *
 * - Connects to `./data/tasks.db` using the SQLite JDBC driver.
 * - Creates the `task` table if missing.
 * - Seeds example tasks when the table is empty.
 */
fun configureDatabases() {
    Database.connect(
        url = "jdbc:sqlite:./server/data/tasks.db",
        driver = "org.sqlite.JDBC"
    )

    transaction {
        // Create the table if it doesn't exist
        SchemaUtils.create(TaskTable)

        // Seed initial data only when the table is empty
        if (TaskTable.selectAll().empty())
            seedTasks()
    }
}

/**
 * Inserts a small set of sample tasks.
 */
private fun seedTasks() {
    val tasks = listOf(
        Triple("cleaning",   "Clean the house",          "Low"),
        Triple("gardening",  "Mow the lawn",             "Medium"),
        Triple("shopping",   "Buy the groceries",        "High"),
        Triple("painting",   "Paint the fence",          "Medium"),
        Triple("exercising", "Walk the dog",             "Medium"),
        Triple("meditating", "Contemplate the infinite", "High")
    )

    tasks.forEach { (name, description, priority) ->
        TaskDAO.new {
            this.name = name
            this.description = description
            this.priority = priority
        }
    }
}