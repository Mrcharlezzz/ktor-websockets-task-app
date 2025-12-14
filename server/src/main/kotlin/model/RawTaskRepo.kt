package model

/**
 * In-memory `TaskRepository` for testing and demos.
 */
class RawTaskRepository : TaskRepository{
    private val tasks = mutableListOf(
        Task("cleaning", "Clean the house", Priority.Low),
        Task("gardening", "Mow the lawn", Priority.Medium),
        Task("shopping", "Buy the groceries", Priority.High),
        Task("painting", "Paint the fence", Priority.Medium)
    )

    /** Returns the current in-memory list of tasks. */
    override suspend fun allTasks(): List<Task> = tasks

    /** Filters tasks by [priority]. */
    override suspend fun tasksByPriority(priority: Priority) = tasks.filter {
        it.priority == priority
    }

    /** Looks up a task by [name] ignoring case. */
    override suspend fun taskByName(name: String) = tasks.find {
        it.name.equals(name, ignoreCase = true)
    }

    /** Adds a new [task]; throws if a task with the same name exists. */
    override suspend fun addTask(task: Task) {
        if (taskByName(task.name) != null) {
            throw IllegalStateException("Cannot duplicate task names!")
        }
        tasks.add(task)
    }

    /** Removes the task with [name]; returns true if removed. */
    override suspend fun removeTask(name: String): Boolean {
        return tasks.removeIf { it.name == name }
    }
}