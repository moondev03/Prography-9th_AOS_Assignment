package com.weave.project.model

import com.weave.project.data.remote.dto.Urls
import com.weave.project.data.remote.dto.User

data class PhotoEntity(
    val id: String,
    val title: String?,
    val description: String?,
    val urls: Urls,
    val width: Int,
    val height: Int,
    val user: User?
)
