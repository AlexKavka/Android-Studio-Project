package com.ak.androidstudioproject.AppList.Domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAppUrlsUseCase @Inject constructor(
    private val rep: AppsListRepository
) {
    suspend operator fun invoke(): AppList? {
        return withContext(Dispatchers.IO) {
            rep.getAppUrls()
        }
    }
}

class ValidateAppUrlsUseCase @Inject constructor() {
    operator fun invoke(appUrls: AppList?): Boolean {
        return appUrls != null && appUrls.urls.isNotEmpty()
    }
}

class GetAppPreCardUseCase @Inject constructor(
    private val rep: AppsListRepository
) {
    suspend operator fun invoke(packageName: String): PreCardInfo? {
        return withContext(Dispatchers.IO) {
            rep.getAppPreCard(packageName)
        }
    }
}

class RefreshEventUseCase @Inject constructor() {
    private val _refreshTrigger = MutableSharedFlow<Unit>(replay = 1)
    val refreshTrigger: SharedFlow<Unit> = _refreshTrigger.asSharedFlow()

    suspend fun triggerRefresh() {
        _refreshTrigger.emit(Unit)
    }
}