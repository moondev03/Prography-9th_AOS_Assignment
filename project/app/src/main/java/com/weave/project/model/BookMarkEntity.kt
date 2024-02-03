package com.weave.project.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "table_bookmark")
data class BookMarkEntity (
    @PrimaryKey
    val id: String,
    val url: String,
)