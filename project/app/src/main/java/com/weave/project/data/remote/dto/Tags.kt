package com.weave.project.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Tags(
    @SerializedName("title")
    val title: String
)