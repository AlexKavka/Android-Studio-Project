package com.ak.androidstudioproject.DI

import com.ak.androidstudioproject.AppList.Data.ListMapper
import com.ak.androidstudioproject.AppList.Data.PreCardMapper
import com.ak.androidstudioproject.AppDetailes.Data.AppDetailsMapper
import com.ak.androidstudioproject.AppList.Data.AppsListRepositoryImpl
import com.ak.androidstudioproject.AppDetailes.Data.AppDetailsRepositoryImpl
import com.ak.androidstudioproject.AppDetailes.Data.Local.FullCardDao
import com.ak.androidstudioproject.AppDetailes.Data.Remote.RetrofitApiService
import com.ak.androidstudioproject.AppList.Domain.AppsListRepository
import com.ak.androidstudioproject.AppDetailes.Domain.AppDetailsRepository
import com.ak.androidstudioproject.AppList.Data.Local.PreCardDao
import com.ak.androidstudioproject.AppList.Data.Remote.ListRetrofitApiService
import com.ak.androidstudioproject.AppList.Data.Remote.PreCardRetrofitApiService
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
        preCardApiService: PreCardRetrofitApiService,
        listApiService: ListRetrofitApiService,
        listMapper: ListMapper,
        preCardMapper: PreCardMapper,
        preCardDao : PreCardDao
    ): AppsListRepository {
        return AppsListRepositoryImpl(
            preCardApiService,
            listApiService,
            listMapper,
            preCardMapper,
            preCardDao
        )
    }

    @Provides
    @Singleton
    fun provideAppDetailsRepository(
        appDetailsApiService: RetrofitApiService,
        dao: FullCardDao,
        mapper: AppDetailsMapper
    ): AppDetailsRepository {
        return AppDetailsRepositoryImpl(
            appDetailsApiService,
            dao,
            mapper
        )
    }
}