package com.todo.persistence

import com.todo.models.Todo
import com.todo.persistence.shared.Repositories
import com.todo.persistence.shared.Repositories.withEmptyTable
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TodoRepositoryImplTest {

    @Test
    fun canCreateAndReadNewTodo(): Unit = runBlocking {
        val todoRepository = Repositories.createTodoRepository().withEmptyTable()

        val createdTodo = todoRepository.add(Todo(title = "test", done = false))

        assertThat(createdTodo).isEqualTo(Todo(1, "test", false))
    }

    @Test
    fun canGetTodo(): Unit = runBlocking {
        val todoRepository = Repositories.createTodoRepository().withEmptyTable()
        todoRepository.add(Todo(title = "test", done = false))

        val readTodo = todoRepository.get(1)

        assertThat(readTodo).isEqualTo(Todo(1, "test", false))
    }

}
