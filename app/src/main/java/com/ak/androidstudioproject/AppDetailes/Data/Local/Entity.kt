package com.ak.androidstudioproject.AppDetailes.Data.Local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ak.androidstudioproject.AppDetailes.Data.Remote.AgeRestriction
import com.ak.androidstudioproject.AppDetailes.Data.Remote.FileUrl
import com.ak.androidstudioproject.AppDetailes.Data.Remote.Rating

@Entity(tableName = "full_card_cache")
data class FullCardEntity(
    @PrimaryKey
    val appId: Int,
    val packageName: String,
    val appName: String,
    val categories: List<String>,
    val companyName: String,
    val shortDescription: String,
    val fullDescription: String,
    val fileSize: Long,
    val versionName: String,
    val iconUrl: String,
    val fileUrls: List<FileUrl>,
    val ageRestriction: AgeRestriction,
    val rating: Rating?,
    val downloads: Int?,
    val price: Int?
)
