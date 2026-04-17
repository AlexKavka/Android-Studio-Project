package com.ak.androidstudioproject.AppDetailes.Data

import android.util.Log
import com.ak.androidstudioproject.AppDetailes.Data.Local.FullCardDao
import com.ak.androidstudioproject.AppDetailes.Data.Remote.RetrofitApiService
import com.ak.androidstudioproject.AppDetailes.Domain.AppDetailsRepository
import com.ak.androidstudioproject.AppDetailes.Domain.FullCardInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
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

    override suspend fun toggleWishlist(packageName: String) {
        dao.updateWishlistStatus(packageName)
        val newStatus = dao.getWishlistStatus(packageName)
        Log.d("Repository", "New wishlist status: $newStatus")
    }

    override fun observeAppDetails(packageName: String): Flow<FullCardInfo> {
        Log.d("Repository", "observeAppDetails called for: $packageName")
        return dao.getFullCardFlow(packageName)
            .onEach { entity ->
                Log.d("Repository", "Flow emitted entity: $entity")
                Log.d("Repository", "isInWishlist from DB: ${entity?.isInWishlist}")
            }
            .filterNotNull()
            .map { entity ->
                val domain = mapper.toDomain(entity)
                Log.d("Repository", "Mapped to domain, isInWishlist: ${domain.isInWishlist}")
                domain
            }
    }
}