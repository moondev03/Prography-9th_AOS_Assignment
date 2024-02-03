package com.weave.project.model

import com.weave.project.data.remote.dto.Tags


data class PhotoDetailEntity(
    val id: String,
    val width: Int?,
    val height: Int?,
    val username: String?,
    val url: String,
    val download: String?,
    val title: String?,
    val desc: String?,
    val tags: List<Tags>
)
