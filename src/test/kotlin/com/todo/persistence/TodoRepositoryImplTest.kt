package com.todo.persistence

import com.todo.models.Todo
import com.todo.shared.testApplicationWithEmptyDatabase
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TodoRepositoryImplTest {

    @Test
    fun canCreateAndReadNewTodo(): Unit = testApplicationWithEmptyDatabase {
        val createdTodo = todoRepository.add(Todo(title = "test", done = false))

        assertThat(createdTodo).isEqualTo(Todo(1, "test", false))
    }

    @Test
    fun canGetTodo(): Unit = testApplicationWithEmptyDatabase {
        todoRepository.add(Todo(title = "test", done = false))

        val readTodo = todoRepository.get(1)

        assertThat(readTodo).isEqualTo(Todo(1, "test", false))
    }

}
