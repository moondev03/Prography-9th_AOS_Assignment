package com.weave.project.util

import com.weave.project.data.remote.dto.GetPhotosRes
import com.weave.project.data.remote.dto.GetRandomPhotosRes
import com.weave.project.model.PhotoEntity

fun GetPhotosRes.asEntity() = PhotoEntity(
    id = this.id,
    description = this.description,
    urls = this.urls,
    width = this.width,
    height = this.height
)

fun GetRandomPhotosRes.asEntity() = PhotoEntity(
    id = this.id,
    description = this.description,
    urls = this.urls,
    width = this.width,
    height = this.height
)
