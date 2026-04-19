package com.ak.androidstudioproject.AppDetailes.Data

import com.ak.androidstudioproject.AppDetailes.Data.Local.FullCardEntity
import com.ak.androidstudioproject.AppDetailes.Data.Remote.FullCardDTO
import com.ak.androidstudioproject.AppDetailes.Data.Remote.Rating
import com.ak.androidstudioproject.AppDetailes.Domain.FullCardInfo
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.String

@Singleton
class AppDetailsMapper @Inject constructor(){

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

    fun toDomain(entity: FullCardEntity?): FullCardInfo {
        return FullCardInfo(
            url = entity?.packageName ?: "",
            appName = entity?.appName ?: "",
            shortDescription = entity?.shortDescription ?: "",
            iconUrl = entity?.iconUrl ?: "",
            categories = entity?.categories ?: emptyList(),
            screenshots = entity?.fileUrls?.map { it.fileUrl } ?: emptyList(),
            ageRating = entity?.ageRestriction?.category ?: "",
            developer = entity?.companyName ?: "",
            appSize = entity?.fileSize ?: 0,
            isInWishlist = entity?.isInWishlist ?: false
        )
    }

    fun toEntity(packageName: String, dto: FullCardDTO): FullCardEntity {
        val body = dto.body

        return FullCardEntity(
            appId = body.appId,
            packageName = packageName,
            appName = body.appName,
            categories = body.categories,
            companyName = body.companyName,
            shortDescription = body.shortDescription,
            fullDescription = body.fullDescription,
            fileSize = body.fileSize,
            versionName = body.versionName,
            iconUrl = body.iconUrl,
            fileUrls = body.fileUrls,
            ageRestriction = body.ageRestriction,
            rating = body.rating as Rating?,
            downloads = body.downloads,
            price = body.price
        )
    }
}