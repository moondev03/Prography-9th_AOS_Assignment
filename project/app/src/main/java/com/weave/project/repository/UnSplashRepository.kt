package com.weave.project.repository

import com.weave.project.data.remote.Result
import com.weave.project.model.PhotoDetailEntity
import com.weave.project.model.PhotoEntity


interface UnSplashRepository {
    suspend fun getPhotos(page: Int, perPage: Int): Result<List<PhotoEntity>>

    suspend fun getRandomPhotos(count: Int): Result<List<PhotoEntity>>

    suspend fun getPhotoInfo(id: String): Result<PhotoDetailEntity>
}