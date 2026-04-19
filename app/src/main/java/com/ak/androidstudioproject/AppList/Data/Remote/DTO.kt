package com.ak.androidstudioproject.AppList.Data.Remote

import com.google.gson.annotations.SerializedName

data class AppListDTO(
    val html : String?
)

data class PreCardDTO(
    val code: String,
    val message: String?,
    val body: PreCardBody,
    val timestamp: String
)

data class PreCardBody(
    @SerializedName("packageName") val packageName: String,
    @SerializedName("appName") val appName: String,
    @SerializedName("categories") val categories: List<String>,
    @SerializedName("shortDescription") val shortDescription: String,
    @SerializedName("iconUrl") val iconUrl: String
)