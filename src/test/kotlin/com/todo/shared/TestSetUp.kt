package com.todo.shared

import com.todo.persistence.Todos
import com.todo.plugins.createDatabase
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun testApplicationWithEmptyDatabase(block: suspend ApplicationTestBuilder.() -> Unit) {
    testApplication {
        application {
            transaction(createDatabase()) {
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
