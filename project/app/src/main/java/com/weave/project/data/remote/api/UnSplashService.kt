package com.weave.project.data.remote.api

import com.weave.project.data.remote.dto.GetPhotoDetailRes
import com.weave.project.data.remote.dto.GetPhotosRes
import com.weave.project.data.remote.dto.GetRandomPhotosRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UnSplashService {
    @GET("/photos")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<List<GetPhotosRes>>

    @GET("/photos/random")
    suspend fun getRandomPhotos(
        @Query("count") count: Int
    ): Response<List<GetRandomPhotosRes>>

    @GET("/photos/{id}")
    suspend fun getPhotoInfo(
        @Path("id") id: String
    ): Response<GetPhotoDetailRes>
}