package com.ak.androidstudioproject.AppList.Data

import com.ak.androidstudioproject.AppList.Data.*
import com.ak.androidstudioproject.AppList.Domain.AppList
import com.ak.androidstudioproject.AppList.Domain.PreCardInfo
import com.google.gson.Gson

class PreCardMapper {
    //private val gson = Gson()

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
}

class ListMapper {
    private val gson = Gson()

    fun toDomain(dto: AppListDTO?): AppList? {
        return dto?.let { response ->

            AppList(
                urls = response.urls
            )
        }
    }
}