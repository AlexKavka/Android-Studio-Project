package com.ak.androidstudioproject.AppList.Data.Local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ak.androidstudioproject.AppList.Data.Remote.PreCardDTO
import com.google.gson.Gson

@Entity(tableName = "pre_card_cache")
data class PreCardEntity(
    @PrimaryKey
    val packageName: String,
    val appName: String,
    val iconUrl: String,
    val shortDescription: String,
    val categories: List<String>
) {
    companion object {
        fun fromDto(packageName: String, dto: PreCardDTO): PreCardEntity {
            val body = dto.body
            return PreCardEntity(
                packageName = packageName,
                appName = body?.appName ?: "",
                iconUrl = body?.iconUrl ?: "",
                shortDescription = body?.shortDescription ?: "",
                categories = body.categories
            )
        }
    }
}