package com.todo.models

import kotlinx.serialization.Serializable

@Serializable
data class Todo(
    val id: Long? = null,
    val title: String,
    val done: Boolean
    )

data class TodoList(
    val id: Long,
    val todos: List<Todo>)

interface TodoRepository {
    suspend fun add(todo: Todo): Todo?
    suspend fun get(id: Long): Todo?
}

