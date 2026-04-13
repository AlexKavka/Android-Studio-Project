package com.ak.androidstudioproject.AppDetailes.Data

import com.google.gson.annotations.SerializedName

data class FullCardDTO(
    val code: String,
    val message: String?,
    val body: AppBody,
    val timestamp: String
)

data class AppBody(
    @SerializedName("appId") val appId: Int,
    @SerializedName("packageName") val packageName: String,
    @SerializedName("appName") val appName: String,
    @SerializedName("categories") val categories: List<String>,
    @SerializedName("companyName") val companyName: String,
    @SerializedName("shortDescription") val shortDescription: String,
    @SerializedName("fullDescription") val fullDescription: String,
    @SerializedName("fileSize") val fileSize: Long,
    @SerializedName("versionName") val versionName: String,
    @SerializedName("iconUrl") val iconUrl: String,
    @SerializedName("fileUrls") val fileUrls: List<FileUrl>,
    @SerializedName("ageRestriction") val ageRestriction: AgeRestriction,
    @SerializedName("rating") val rating: Rating?,
    @SerializedName("downloads") val downloads: Int?,
    @SerializedName("price") val price: Int?
)

data class AgeRestriction(
    @SerializedName("category") val category: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("imageUrl") val imageUrl: String
)

data class Rating(
    @SerializedName("average") val average: Double,
    @SerializedName("votes") val votes: Int
)

data class FileUrl(
    @SerializedName("fileUrl") val fileUrl: String,
    @SerializedName("ordinal") val ordinal: Int,
    @SerializedName("type") val type: String,
    @SerializedName("orientation") val orientation: String
)