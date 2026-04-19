package com.ak.androidstudioproject.DI

import android.content.Context
import com.ak.androidstudioproject.AppDetailes.Data.Local.AppDatabase
import com.ak.androidstudioproject.AppDetailes.Data.Local.FullCardDao
import com.ak.androidstudioproject.AppList.Data.Local.AppListDatabase
import com.ak.androidstudioproject.AppList.Data.Local.PreCardDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideFullCardDao(database: AppDatabase): FullCardDao {
        return database.fullCardDao()
    }

    @Provides
    @Singleton
    fun provideAppListDatabase(@ApplicationContext context: Context): AppListDatabase {
        return AppListDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun providePreCardDao(database: AppListDatabase): PreCardDao {
        return database.preCardDao()
    }
}