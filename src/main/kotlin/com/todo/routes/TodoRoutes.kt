package com.todo.routes

import com.todo.models.Todo
import com.todo.persistence.todoRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.todoRouting() {
    route("todos") {
        post {
            val todo = call.receive<Todo>()
            val createdTodo = todoRepository.add(todo) ?: ""
            call.respond(message = createdTodo, status = HttpStatusCode.Created)
        }
        get("{id?}") {
            val id = call.parameters["id"] ?:
            return@get call.respondText("Missing id", status = HttpStatusCode.BadRequest)

            val todoId = id.toLongOrNull() ?:
            return@get call.respondText("Invalid id", status = HttpStatusCode.BadRequest)

            val todo = todoRepository.get(todoId) ?:
            return@get call.respondText("Not Found", status = HttpStatusCode.NotFound)

            call.respond(message = todo, status = HttpStatusCode.OK)
        }
    }
}
