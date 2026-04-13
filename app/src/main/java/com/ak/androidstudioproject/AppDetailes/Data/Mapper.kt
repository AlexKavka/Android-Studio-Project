package com.ak.androidstudioproject.AppDetailes.Data

import com.ak.androidstudioproject.AppDetailes.Domain.FullCardInfo
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlin.collections.set

class AppDetailsMapper {
    private val gson = Gson()

    fun toDomain(dto: FullCardDTO?): FullCardInfo? {
        return dto?.let { response ->

            val body = response.body

            FullCardInfo(
                url = body.packageName,
                appName = body.appName,
                shortDescription = body.shortDescription,
                iconUrl = body.iconUrl,
                categories = body.categories,
                screenshots = body.fileUrls.map { it.fileUrl },
                ageRating = body.ageRestriction.category,
                developer = body.companyName,
                appSize = body.fileSize
            )
        }
    }
}