package com.todo.persistence

import org.jetbrains.exposed.sql.Table

object Todos : Table() {
    val id = long("id").autoIncrement()
    val title = varchar("title", 128)
    val done = bool("done")

    override val primaryKey = PrimaryKey(id)
}
