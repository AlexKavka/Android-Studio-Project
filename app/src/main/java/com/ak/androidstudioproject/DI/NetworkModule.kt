package com.ak.androidstudioproject.DI

import com.ak.androidstudioproject.AppDetailes.Data.ApiService
import com.ak.androidstudioproject.AppList.Data.ListApiService
import com.ak.androidstudioproject.AppList.Data.PreCardApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideListApiService(): ListApiService {
        return ListApiService()
    }

    @Provides
    @Singleton
    fun providePreCardApiService(): PreCardApiService {
        return PreCardApiService()
    }

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return ApiService()
    }
}