package com.todo.plugins

import com.todo.routes.todoRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        todoRouting()
    }
}
