package com.ak.androidstudioproject.AppDetailes.Domain

import kotlinx.coroutines.flow.Flow

interface AppDetailsRepository {
    suspend fun getFullAppInfo(packageName: String) : FullCardInfo?

    suspend fun toggleWishlist(packageName: String)

    fun observeAppDetails(packageName: String): Flow<FullCardInfo>
}