package com.ak.androidstudioproject.AppList.Domain

interface AppsListRepository {
    suspend fun getAppUrls(): AppList?
    suspend fun getAppPreCard(packageName: String) : PreCardInfo?
    suspend fun clearCache()
}