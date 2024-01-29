package com.weave.project.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Current_user_collections(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("published_at")
    val published_at: String,
    @SerializedName("last_collected_at")
    val last_collected_at: String,
    @SerializedName("updated_at")
    val updated_at: String
)