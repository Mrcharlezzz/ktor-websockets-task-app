package com.example

import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.server.application.*
import io.ktor.server.routing.routing
import io.ktor.server.websocket.*
import io.ktor.websocket.CloseReason
import io.ktor.websocket.close
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import com.example.model.Task
import com.example.model.TaskRepository
import java.util.Collections
import kotlin.time.Duration.Companion.seconds

/**
 * Installs WebSockets and exposes task-related WS endpoints.
 *
 * Endpoints:
 * - WS `/tasks` → streams all tasks once then closes with NORMAL reason.
 * - WS `/tasks2` →
 *   - on connect: streams all tasks;
 *   - then accepts `Task` JSON frames to add new tasks and broadcasts them to all connected sessions.
 *
 * Messages are serialized with Kotlinx (JSON).
 */
fun Application.configureSockets(taskRepository: TaskRepository) {
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
        pingPeriod = 15.seconds
        timeout = 15.seconds
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        val sessions =
            Collections.synchronizedList<WebSocketServerSession>(ArrayList())

        webSocket("/tasks") {
            sendAllTasks(taskRepository)
            close(CloseReason(CloseReason.Codes.NORMAL, "All done"))
        }

        webSocket("/tasks2") {
            sessions.add(this)
            try {
                sendAllTasks(taskRepository)

                while (true) {
                    val newTask = receiveDeserialized<Task>()
                    taskRepository.addTask(newTask)
                    for (session in sessions) {
                        session.sendSerialized(newTask)
                    }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                sessions.remove(this)
            }
        }
    }
}

/**
 * Sends every task to the current WebSocket session with a small delay between frames.
 */
private suspend fun DefaultWebSocketServerSession.sendAllTasks(taskRepository: TaskRepository) {
    for (task in taskRepository.allTasks()) {
        sendSerialized(task)
        delay(1000)
    }
}

