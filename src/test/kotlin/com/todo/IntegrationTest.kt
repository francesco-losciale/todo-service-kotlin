package com.todo

import com.todo.models.Todo
import com.todo.shared.httpClient
import com.todo.shared.testApplicationWithEmptyDatabase
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.http.HttpStatusCode.Companion.UnsupportedMediaType
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class IntegrationTest {

    @Test
    fun canCreateATodoItem() = testApplicationWithEmptyDatabase {
        val expectedCreatedTodo = Todo(id = 1, title = "A todo item", done = false)

        val response = httpClient().post("/todos") {
            contentType(ContentType.Application.Json)
            setBody(Todo(title = "A todo item", done = false))
        }
        val result = Json.decodeFromString(response.bodyAsText()) as Todo

        assertThat(response.status).isEqualTo(Created)
        assertThat(result).isEqualTo(expectedCreatedTodo)
    }

    @Test
    fun canGetATodoItem() = testApplicationWithEmptyDatabase {
        val postResponse = httpClient().post("/todos") {
            contentType(ContentType.Application.Json)
            setBody(Todo(title = "A todo item", done = false))
        }
        val createdTodo = Json.decodeFromString(postResponse.bodyAsText()) as Todo

        val response = httpClient().get("/todos/${createdTodo.id}") {
            contentType(ContentType.Application.Json)
        }
        val result = Json.decodeFromString(response.bodyAsText()) as Todo

        assertThat(response.status).isEqualTo(OK)
        assertThat(result).hasFieldOrProperty("title")
        assertThat(result).hasFieldOrProperty("done")
        assertThat(result).hasFieldOrProperty("id")
    }

    @Test
    fun failsPostWithBadRequest() = testApplicationWithEmptyDatabase {
        val response = httpClient().post("/todos") {
            contentType(ContentType.Application.Json)
            setBody("{}")
        }

        assertThat(response.status).isEqualTo(BadRequest)
    }

    @Test
    fun failsPostWithUnsupportedMediaType() = testApplicationWithEmptyDatabase {
        val response = httpClient().post("/todos") {
            contentType(ContentType.Application.Json)
        }

        assertThat(response.status).isEqualTo(UnsupportedMediaType)
    }

    @Test
    fun failsGetWithMissingPathParameter() = testApplicationWithEmptyDatabase {
        val response = httpClient().get("/todos/") {
            contentType(ContentType.Application.Json)
        }

        assertThat(response.status).isEqualTo(BadRequest)
    }

    @Test
    fun failsGetWithInvalidPathParameter() = testApplicationWithEmptyDatabase {
        val response = httpClient().get("/todos/invalid") {
            contentType(ContentType.Application.Json)
        }

        assertThat(response.status).isEqualTo(BadRequest)
    }

    @Test
    fun failsGetWithResourceNotFound() = testApplicationWithEmptyDatabase {
        val response = httpClient().get("/todos/1") {
            contentType(ContentType.Application.Json)
        }

        assertThat(response.status).isEqualTo(NotFound)
    }

}
