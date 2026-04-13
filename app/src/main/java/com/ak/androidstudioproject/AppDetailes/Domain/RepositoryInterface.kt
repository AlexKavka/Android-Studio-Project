package com.ak.androidstudioproject.AppDetailes.Domain

interface AppDetailsRepository {
    suspend fun getFullAppInfo(packageName: String) : FullCardInfo?
}



