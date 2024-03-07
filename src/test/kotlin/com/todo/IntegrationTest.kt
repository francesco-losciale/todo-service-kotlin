package com.todo

import com.todo.models.Todo
import com.todo.models.TodoRepository
import com.todo.persistence.Todos
import com.todo.persistence.shared.Repositories
import com.todo.persistence.shared.Repositories.truncate
import com.todo.persistence.shared.Repositories.withEmptyTable
import com.typesafe.config.ConfigFactory
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.http.HttpStatusCode.Companion.UnsupportedMediaType
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class IntegrationTest {

    private lateinit var todoRepository: TodoRepository

    @Before
    fun setUp() {
        todoRepository = Repositories.createTodoRepository().withEmptyTable()
    }

    @After
    fun tearDown() {
        todoRepository.truncate()
    }

    @Test
    fun canCreateATodoItem() = testApplication {
        environment {
            config = ApplicationConfig("application.yaml")
        }
        val client = httpClient()
        val expectedCreatedTodo = Todo(id = 1, title = "A todo item", done = false)

        val response = client.post("/todos") {
            contentType(ContentType.Application.Json)
            setBody(Todo(title = "A todo item", done = false))
        }
        val result = Json.decodeFromString(response.bodyAsText()) as Todo

        assertThat(response.status).isEqualTo(Created)
        assertThat(result).isEqualTo(expectedCreatedTodo)
    }

    @Test
    fun canGetATodoItem() = testApplication {
        val client = httpClient()
        val postResponse = client.post("/todos") {
            contentType(ContentType.Application.Json)
            setBody(Todo(title = "A todo item", done = false))
        }
        val createdTodo = Json.decodeFromString(postResponse.bodyAsText()) as Todo

        val response = client.get("/todos/${createdTodo.id}") {
            contentType(ContentType.Application.Json)
        }
        val result = Json.decodeFromString(response.bodyAsText()) as Todo

        assertThat(response.status).isEqualTo(OK)
        assertThat(result).hasFieldOrProperty("title")
        assertThat(result).hasFieldOrProperty("done")
        assertThat(result).hasFieldOrProperty("id")
    }

    @Test
    fun failsPostWithBadRequest() = testApplication {
        val client = httpClient()

        val response = client.post("/todos") {
            contentType(ContentType.Application.Json)
            setBody("{}")
        }

        assertThat(response.status).isEqualTo(BadRequest)
    }

    @Test
    fun failsPostWithUnsupportedMediaType() = testApplication {
        val client = httpClient()

        val response = client.post("/todos") {
            contentType(ContentType.Application.Json)
        }

        assertThat(response.status).isEqualTo(UnsupportedMediaType)
    }

    @Test
    fun failsGetWithMissingPathParameter() = testApplication {
        val client = httpClient()

        val response = client.get("/todos/") {
            contentType(ContentType.Application.Json)
        }

        assertThat(response.status).isEqualTo(BadRequest)
    }

    @Test
    fun failsGetWithInvalidPathParameter() = testApplication {
        val client = httpClient()

        val response = client.get("/todos/invalid") {
            contentType(ContentType.Application.Json)
        }

        assertThat(response.status).isEqualTo(BadRequest)
    }

    @Test
    fun failsGetWithResourceNotFound() = testApplication {
        val client = httpClient()

        val response = client.get("/todos/1") {
            contentType(ContentType.Application.Json)
        }

        assertThat(response.status).isEqualTo(NotFound)
    }

    private fun ApplicationTestBuilder.httpClient(): HttpClient {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        return client
    }
}
