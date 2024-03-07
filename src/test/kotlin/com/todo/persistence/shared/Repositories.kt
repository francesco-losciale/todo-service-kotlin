package com.todo.persistence.shared

import com.todo.models.TodoRepository
import com.todo.persistence.TodoRepositoryImpl
import com.todo.persistence.Todos
import com.typesafe.config.ConfigFactory
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction

object Repositories {

    private val database = createDatabase()

    fun createTodoRepository(): TodoRepository {
        return TodoRepositoryImpl()
    }

    fun TodoRepository.withEmptyTable(): TodoRepository {
        transaction(database) {
            SchemaUtils.drop(Todos)
            SchemaUtils.create(Todos)
        }
        return this
    }

    fun TodoRepository.truncate(): TodoRepository {
        transaction(database) {
            Todos.deleteAll()
        }
        return this
    }

    private fun createDatabase(): Database {
        val config = ConfigFactory.load("application.conf")
        return Database.connect(
            url = config.getString("database.url"),
            driver = config.getString("database.driver"),
            user = config.getString("database.user"),
            password = config.getString("database.password")
        )
    }
}
