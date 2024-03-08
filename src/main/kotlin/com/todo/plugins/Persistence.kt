package com.todo.plugins

import com.todo.persistence.Todos
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.createDatabase(): Database {
    val database = Database.connect(
        url = environment.config.propertyOrNull("database.url")!!.getString(),
        driver = environment.config.propertyOrNull("database.driver")!!.getString(),
        user = environment.config.propertyOrNull("database.user")!!.getString(),
        password = environment.config.propertyOrNull("database.password")!!.getString()
    )
    transaction(database) {
        SchemaUtils.create(Todos)
    }
    return database
}
