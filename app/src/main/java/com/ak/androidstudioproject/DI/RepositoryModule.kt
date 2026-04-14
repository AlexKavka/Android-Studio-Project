package com.ak.androidstudioproject.DI

import com.ak.androidstudioproject.AppDetailes.Data.ApiService
import com.ak.androidstudioproject.AppList.Data.ListApiService
import com.ak.androidstudioproject.AppList.Data.PreCardApiService
import com.ak.androidstudioproject.AppList.Data.ListMapper
import com.ak.androidstudioproject.AppList.Data.PreCardMapper
import com.ak.androidstudioproject.AppDetailes.Data.AppDetailsMapper
import com.ak.androidstudioproject.AppList.Data.AppsListRepositoryImpl
import com.ak.androidstudioproject.AppDetailes.Data.AppDetailsRepositoryImpl
import com.ak.androidstudioproject.AppList.Domain.AppsListRepository
import com.ak.androidstudioproject.AppDetailes.Domain.AppDetailsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAppsListRepository(
        preCardApiService: PreCardApiService,
        listApiService: ListApiService,
        listMapper: ListMapper,
        preCardMapper: PreCardMapper
    ): AppsListRepository {
        return AppsListRepositoryImpl(
            preCardApiService,
            listApiService,
            listMapper,
            preCardMapper
        )
    }

    @Provides
    @Singleton
    fun provideAppDetailsRepository(
        appDetailsApiService: ApiService,
        appDetailsMapper: AppDetailsMapper
    ): AppDetailsRepository {
        return AppDetailsRepositoryImpl(
            appDetailsApiService,
            appDetailsMapper
        )
    }
}