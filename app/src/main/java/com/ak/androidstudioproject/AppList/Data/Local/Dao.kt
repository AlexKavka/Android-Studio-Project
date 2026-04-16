package com.ak.androidstudioproject.AppList.Data.Local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ak.androidstudioproject.AppList.Data.Local.PreCardEntity

@Dao
interface PreCardDao {

    @Query("SELECT * FROM pre_card_cache WHERE packageName = :packageName")
    suspend fun getPreCard(packageName: String): PreCardEntity?

    @Query("SELECT packageName FROM pre_card_cache")
    suspend fun getAllPackageNames(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreCard(entity: PreCardEntity)

    @Query("DELETE FROM pre_card_cache")
    suspend fun clearAll()
}