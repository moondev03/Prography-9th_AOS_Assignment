package com.weave.project.util

import com.weave.project.data.remote.dto.GetPhotoDetailRes
import com.weave.project.data.remote.dto.GetPhotosRes
import com.weave.project.data.remote.dto.GetRandomPhotosRes
import com.weave.project.model.BookMarkEntity
import com.weave.project.model.PhotoEntity

fun GetPhotosRes.asEntity() = PhotoEntity(
    id = this.id,
    description = this.description,
    urls = this.urls,
    width = this.width,
    height = this.height,
    user = this.user
)

fun GetRandomPhotosRes.asEntity() = PhotoEntity(
    id = this.id,
    description = this.description,
    urls = this.urls,
    width = this.width,
    height = this.height,
    user = null
)

fun GetPhotoDetailRes.asEntity() = BookMarkEntity(
    id = this.id,
    url = this.urls.regular
)

fun PhotoEntity.asBookMark() = BookMarkEntity(
    id = this.id,
    url = this.urls.regular
)