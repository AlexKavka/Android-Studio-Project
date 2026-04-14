package com.ak.androidstudioproject.AppList.Data

import com.ak.androidstudioproject.AppList.Data.Local.PreCardDao
import com.ak.androidstudioproject.AppList.Data.Remote.AppListDTO
import com.ak.androidstudioproject.AppList.Data.Remote.ListRetrofitApiService
import com.ak.androidstudioproject.AppList.Data.Remote.PreCardDTO
import com.ak.androidstudioproject.AppList.Data.Remote.PreCardRetrofitApiService
import com.ak.androidstudioproject.AppList.Domain.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppsListRepositoryImpl @Inject constructor(
    private val preCardApiService: PreCardRetrofitApiService,
    private val listApiService: ListRetrofitApiService,
    private val listMapper: ListMapper,
    private val preCardMapper: PreCardMapper,
    private val preCardDao: PreCardDao
) : AppsListRepository {

    private val preCardCache =   mutableMapOf<String, PreCardInfo>()

    override suspend fun getAppPreCard(packageName: String): PreCardInfo? {

        val cachedEntity = preCardDao.getPreCard(packageName)

        if (cachedEntity != null) {
            return preCardMapper.toDomain(cachedEntity)
        }

        return try {
            val dto = preCardApiService.getAppInfo(packageName)
            val domain = preCardMapper.toDomain(dto)

            if (domain != null) {
                val entity = preCardMapper.toEntity(packageName, dto)
                preCardDao.insertPreCard(entity)
            }

            domain
        } catch (e: Exception) {
            cachedEntity?.let { return preCardMapper.toDomain(it) }
            null
        }
    }

    override suspend fun getAppUrls(): AppList? {

        return try {
            val responseDTO : String = listApiService.getAppList(query = "a")
            val responseDomain = listMapper.toDomain(responseDTO)

            responseDomain
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun clearCache() {
        preCardDao.clearAll()
    }
}