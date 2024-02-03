package com.weave.project.model

import androidx.room.Entity


@Entity(tableName = "table_bookmark")
data class BookMarkEntity (
    val id: String,
    val width: Int,
    val height: Int,
    val description: String,
    val url: String,
    val download: String,
    val userName: String
)