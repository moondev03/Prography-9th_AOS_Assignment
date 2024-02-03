package com.weave.project.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GetRandomPhotosRes(

    @SerializedName("id")
    val id: String,
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("updated_at")
    val updated_at: String,
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("color")
    val color: String,
    @SerializedName("blur_hash")
    val blur_hash: String,
    @SerializedName("downloads")
    val downloads: Int,
    @SerializedName("likes")
    val likes: Int,
    @SerializedName("liked_by_user")
    val liked_by_user: Boolean,
    @SerializedName("description")
    val description: String,
    @SerializedName("exif")
    val exif: Exif,
    @SerializedName("location")
    val location: Location,
    @SerializedName("current_user_collections")
    val current_user_collections: List<Current_user_collections>,
    @SerializedName("urls")
    val urls: Urls,
    @SerializedName("links")
    val links: Links,
    @SerializedName("user")
    val user: User
)

