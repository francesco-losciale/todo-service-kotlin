package com.todo.shared

import com.todo.persistence.Todos
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun testApplicationWithEmptyDatabase(block: suspend ApplicationTestBuilder.() -> Unit) {
    testApplication {
        application {
            val database = Database.connect(
                url = environment.config.propertyOrNull("database.url")!!.getString(),
                driver = environment.config.propertyOrNull("database.driver")!!.getString(),
                user = environment.config.propertyOrNull("database.user")!!.getString(),
                password = environment.config.propertyOrNull("database.password")!!.getString()
            )
            transaction(database) {
                SchemaUtils.drop(Todos)
                SchemaUtils.create(Todos)
            }
        }
        block()
    }
}

fun ApplicationTestBuilder.httpClient(): HttpClient {
    val client = createClient {
        install(ContentNegotiation) {
            json()
        }
    }
    return client
}
