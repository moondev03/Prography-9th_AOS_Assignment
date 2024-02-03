package com.weave.project.data.remote.dto

import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("id")
    val id: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("portfolio_url")
    val portfolio_url: String,
    @SerializedName("bio")
    val bio: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("total_likes")
    val total_likes: Int,
    @SerializedName("total_photos")
    val total_photos: Int,
    @SerializedName("total_collections")
    val total_collections: Int,
    @SerializedName("instagram_username")
    val instagram_username: String,
    @SerializedName("twitter_username")
    val twitter_username: String,
    @SerializedName("profile_image")
    val profile_image: Profile_image,
    @SerializedName("links")
    val links: UserLinks
)

data class UserLinks(
    @SerializedName("self")
    val self: String,
    @SerializedName("html")
    val html: String,
    @SerializedName("photos")
    val photos: String,
    @SerializedName("likes")
    val likes: String,
    @SerializedName("portfolio")
    val portfolio: String
)

data class Profile_image(
    @SerializedName("small")
    val small: String,
    @SerializedName("medium")
    val medium: String,
    @SerializedName("large")
    val large: String
)