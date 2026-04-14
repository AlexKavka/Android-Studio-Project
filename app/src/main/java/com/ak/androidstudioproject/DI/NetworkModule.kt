package com.ak.androidstudioproject.DI

import com.ak.androidstudioproject.AppDetailes.Data.Remote.RetrofitApiService
import com.ak.androidstudioproject.AppDetailes.Data.Remote.RetrofitClient
import com.ak.androidstudioproject.AppList.Data.Remote.RetrofitClient as ListRetrofitClient
import com.ak.androidstudioproject.AppList.Data.Remote.PreCardRetrofitApiService
import com.ak.androidstudioproject.AppList.Data.Remote.*
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
    fun provideListApiService(): ListRetrofitApiService {
        return ListRetrofitClient.listApiService
    }

    @Provides
    @Singleton
    fun providePreCardApiService(): PreCardRetrofitApiService {
        return ListRetrofitClient.preCardApiService
    }

    @Provides
    @Singleton
    fun provideRetrofitApiService(): RetrofitApiService {
        return RetrofitClient.apiService
    }
}