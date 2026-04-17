package com.ak.androidstudioproject.AppList.Data.Local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [PreCardEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppListDatabase : RoomDatabase() {

    abstract fun preCardDao(): PreCardDao

    companion object {
        @Volatile
        private var INSTANCE: AppListDatabase? = null

        fun getInstance(context: Context): AppListDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppListDatabase::class.java,
                    "app_list_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}