package com.ak.androidstudioproject.AppDetailes.Data

import com.ak.androidstudioproject.AppDetailes.Data.Local.FullCardDao
import com.ak.androidstudioproject.AppDetailes.Data.Remote.FullCardDTO
import com.ak.androidstudioproject.AppDetailes.Data.Remote.RetrofitApiService
import com.ak.androidstudioproject.AppDetailes.Domain.AppDetailsRepository
import com.ak.androidstudioproject.AppDetailes.Domain.FullCardInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDetailsRepositoryImpl @Inject constructor(
    private val apiService: RetrofitApiService,
    private val dao: FullCardDao,
    private val mapper: AppDetailsMapper
) : AppDetailsRepository {

    override suspend fun getFullAppInfo(packageName: String): FullCardInfo? {

        val cachedEntity = dao.getFullCard(packageName)

        if (cachedEntity != null) {
            return mapper.toDomain(cachedEntity)
        }

        return try {
            val dto = apiService.getAppInfo(packageName)
            val domain = mapper.toDomain(dto)

            if (domain != null) {
                val entity = mapper.toEntity(packageName, dto)
                dao.insertFullCard(entity)
            }

            domain
        } catch (e: Exception) {
            null
        }
    }
}