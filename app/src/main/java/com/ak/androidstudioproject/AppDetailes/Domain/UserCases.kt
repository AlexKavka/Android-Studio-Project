package com.ak.androidstudioproject.AppDetailes.Domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetFullAppInfoUseCase @Inject constructor(
    private val rep: AppDetailsRepository
) {
    suspend operator fun invoke(packageName: String): FullCardInfo? {
        return withContext(Dispatchers.IO) {
            rep.getFullAppInfo(packageName)
        }
    }
}

class ObserveAppDetailsUseCase @Inject constructor(
    private val rep: AppDetailsRepository
) {
    operator fun invoke(packageName: String): Flow<FullCardInfo> {
        return rep.observeAppDetails(packageName)
    }
}

class ToggleWishlistUseCase @Inject constructor(
    private val rep: AppDetailsRepository
) {
    suspend operator fun invoke(packageName: String) {
        withContext(Dispatchers.IO) {
            rep.toggleWishlist(packageName)
        }
    }
}