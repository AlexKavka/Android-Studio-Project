package com.ak.androidstudioproject.AppList.Data

import com.ak.androidstudioproject.AppList.Data.Local.PreCardEntity
import com.ak.androidstudioproject.AppList.Data.Remote.PreCardDTO
import com.ak.androidstudioproject.AppList.Domain.AppList
import com.ak.androidstudioproject.AppList.Domain.PreCardInfo
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreCardMapper @Inject constructor() {

    fun toDomain(dto: PreCardDTO?): PreCardInfo? {
        return dto?.let { response ->

            val body = response.body

            PreCardInfo(
                url = body.packageName,
                appName = body.appName,
                shortDescription = body.shortDescription,
                iconUrl = body.iconUrl,
                categories = body.categories
            )
        }
    }

    fun toEntity(packageName: String, dto: PreCardDTO): PreCardEntity {
        return PreCardEntity.fromDto(packageName, dto)
    }

    fun toDomain(entity: PreCardEntity): PreCardInfo {
        return PreCardInfo(
            url = entity.packageName,
            appName = entity.appName,
            shortDescription = entity.shortDescription,
            iconUrl = entity.iconUrl,
            categories = entity.categories
        )
    }
}

@Singleton
class ListMapper @Inject constructor(){
    private val gson = Gson()

    fun extractPackageNamesFromHtml(html: String): List<String> {
        val packageNames = mutableSetOf<String>()

        val patterns = listOf(
            Regex("\"packageName\":\"([^\"]+)\""),
            Regex("packageName=([a-zA-Z0-9._]+)"),
            Regex("/catalog/app/([a-zA-Z0-9._]+)\"")
        )

        for (pattern in patterns) {
            pattern.findAll(html).forEach { match ->
                match.groupValues.getOrNull(1)?.let { pkg ->
                    if (pkg.isNotEmpty() && !pkg.contains("html") && !pkg.contains("script")) {
                        packageNames.add(pkg)
                    }
                }
            }
        }

        return packageNames.toList()
    }

    fun toDomain(dto: String?): AppList? {
        return dto?.let { responseDto ->
            if (responseDto.isNullOrBlank()) {
                return null
            }

            val extractedPackageNames = extractPackageNamesFromHtml(responseDto)

            if (extractedPackageNames.isNotEmpty()) {
                AppList(urls = extractedPackageNames)
            } else {
                null
            }
        } ?: run {
            null
        }
    }
}