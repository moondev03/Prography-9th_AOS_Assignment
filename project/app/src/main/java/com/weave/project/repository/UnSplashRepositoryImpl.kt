package com.weave.project.repository

import com.weave.project.data.remote.RetrofitClient
import com.weave.project.data.remote.api.UnSplashService
import com.weave.project.data.remote.Result
import com.weave.project.model.PhotoEntity
import com.weave.project.util.asEntity


class UnSplashRepositoryImpl: UnSplashRepository {
    private val service = RetrofitClient.getInstance().create(UnSplashService::class.java)

    override suspend fun getPhotos(page: Int, perPage: Int): Result<List<PhotoEntity>> {
        return try {
            val res = service.getPhotos(page, perPage)

            if(res.isSuccessful){
                val data = res.body()
                if(data != null){
                    val photoEntities = data.map { it.asEntity() }
                    Result.Success(photoEntities)
                } else {
                    Result.Error(res.code(), "Received null data")
                }
            } else {
                Result.Error(res.code(), res.message())
            }
        } catch (e: Exception){
            Result.Error(0, e.message ?: "An error occurred")
        }
    }

    override suspend fun getRandomPhotos(count: Int): Result<List<PhotoEntity>> {
        return try {
            val res = service.getRandomPhotos(count)

            if(res.isSuccessful){
                val data = res.body()
                if(data != null){
                    val photoEntities = data.map { it.asEntity() }
                    Result.Success(photoEntities)
                } else {
                    Result.Error(res.code(), "Received null data")
                }
            } else {
                Result.Error(res.code(), res.message())
            }
        } catch (e: Exception){
            Result.Error(0, e.message ?: "An error occurred")
        }
    }
}