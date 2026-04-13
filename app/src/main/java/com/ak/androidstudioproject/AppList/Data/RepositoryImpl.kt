package com.ak.androidstudioproject.AppList.Data

import android.util.Log
import com.ak.androidstudioproject.AppList.Domain.*

class AppsListRepositoryImpl(
    private val preCardApiService: PreCardApiService,
    private val listApiService: ListApiService,
    private val listMapper: ListMapper,
    private val preCardMapper: PreCardMapper
) : AppsListRepository {

    private val preCardCache =   mutableMapOf<String, PreCardInfo>()

    override suspend fun getAppPreCard(packageName: String): PreCardInfo? {

        if (preCardCache[packageName] != null) preCardCache[packageName]?.let {
            return it
        }

        val responseDTO : PreCardDTO? = preCardApiService.getAppInfo(packageName)
        val responseDomain : PreCardInfo? = preCardMapper.toDomain(responseDTO)
        if (responseDomain != null) preCardCache[packageName] = responseDomain

        return responseDomain
    }

    override suspend fun getAppUrls(): AppList? {

        val responseDTO : AppListDTO? = listApiService.getAppList()
        val responseDomain : AppList? = listMapper.toDomain(responseDTO)

        return responseDomain
    }
}