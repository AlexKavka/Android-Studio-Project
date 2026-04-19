package com.ak.androidstudioproject.AppDetailes.Data.Local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FullCardDao {

    @Query("SELECT * FROM full_card_cache WHERE packageName = :packageName")
    suspend fun getFullCard(packageName: String): FullCardEntity

    @Query("SELECT * FROM full_card_cache WHERE packageName = :packageName")
    fun getFullCardFlow(packageName: String): Flow<FullCardEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFullCard(entity: FullCardEntity)

    @Query("DELETE FROM full_card_cache WHERE packageName = :packageName")
    suspend fun deleteFullCard(packageName: String)

    @Query("UPDATE full_card_cache SET isInWishList = NOT isInWishList WHERE packageName = :packageName")
    suspend fun updateWishlistStatus(packageName: String)

    @Query("SELECT isInWishlist FROM full_card_cache WHERE packageName = :packageName")
    suspend fun getWishlistStatus(packageName: String): Boolean

    @Query("DELETE FROM full_card_cache")
    suspend fun clearAll()
}