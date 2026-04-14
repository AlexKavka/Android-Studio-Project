package com.ak.androidstudioproject.DI

import com.ak.androidstudioproject.AppList.Data.ListMapper
import com.ak.androidstudioproject.AppList.Data.PreCardMapper
import com.ak.androidstudioproject.AppDetailes.Data.AppDetailsMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {

    @Provides
    @Singleton
    fun provideListMapper(): ListMapper {
        return ListMapper()
    }

    @Provides
    @Singleton
    fun providePreCardMapper(): PreCardMapper {
        return PreCardMapper()
    }

    @Provides
    @Singleton
    fun provideAppDetailsMapper(): AppDetailsMapper {
        return AppDetailsMapper()
    }
}