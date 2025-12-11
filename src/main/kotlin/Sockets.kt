package com.example

import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.server.application.*
import io.ktor.server.routing.routing
import io.ktor.server.websocket.*
import io.ktor.websocket.CloseReason
import io.ktor.websocket.close
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import model.Task
import model.TaskRepository
import java.util.Collections
import kotlin.time.Duration.Companion.seconds

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

private suspend fun DefaultWebSocketServerSession.sendAllTasks(taskRepository: TaskRepository) {
    for (task in taskRepository.allTasks()) {
        sendSerialized(task)
        delay(1000)
    }
}

