package com.todo.persistence

import com.todo.models.Todo
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class TodoRepository {

    suspend fun add(todo: Todo): Todo? {
        return newSuspendedTransaction(Dispatchers.IO) {
            return@newSuspendedTransaction Todos.insert {
                it[title] = todo.title
                it[done] = todo.done
            }.resultedValues?.singleOrNull()?.let(::resultRowToTodo)
        }
    }

    suspend fun get(id: Long): Todo? {
        return newSuspendedTransaction(Dispatchers.IO) {
            return@newSuspendedTransaction Todos.select { Todos.id eq id}
                .map(::resultRowToTodo)
                .singleOrNull()
        }
    }

    private fun resultRowToTodo(row: ResultRow) = Todo(
        id = row[Todos.id],
        title = row[Todos.title],
        done = row[Todos.done]
    )
}

val todoRepository = TodoRepository()
