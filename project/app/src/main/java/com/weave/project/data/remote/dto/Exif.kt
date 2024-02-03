package com.weave.project.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Exif(
    @SerializedName("make")
    val make: String,
    @SerializedName("model")
    val model: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("exposure_time")
    val exposure_time: String,
    @SerializedName("aperture")
    val aperture: String,
    @SerializedName("focal_length")
    val focal_length: String,
    @SerializedName("iso")
    val iso: Int
)