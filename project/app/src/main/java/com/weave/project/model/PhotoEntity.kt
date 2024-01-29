package com.weave.project.model

import com.weave.project.data.remote.dto.Urls

data class PhotoEntity(
    val id: String?,
    val description: String?,
    val urls: Urls?,
    val width: Int,
    val height: Int
)
