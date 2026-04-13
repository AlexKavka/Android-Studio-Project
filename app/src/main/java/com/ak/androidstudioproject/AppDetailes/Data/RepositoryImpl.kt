package com.ak.androidstudioproject.AppDetailes.Data

import com.ak.androidstudioproject.AppDetailes.Domain.AppDetailsRepository
import com.ak.androidstudioproject.AppDetailes.Domain.FullCardInfo

class AppDetailsRepositoryImpl(
    private val apiService: ApiService,
    private val mapper: AppDetailsMapper
) : AppDetailsRepository {

    private val fullCardCache =   mutableMapOf<String, FullCardInfo>()

    override suspend fun getFullAppInfo(packageName: String): FullCardInfo? {

        fullCardCache[packageName]?.let {
            return it
        }

        val responseDTO : FullCardDTO? = apiService.getAppInfo(packageName)
        val responseDomain : FullCardInfo? = mapper.toDomain(responseDTO)
        if (responseDomain != null) fullCardCache[packageName] = responseDomain

        return responseDomain
    }
}