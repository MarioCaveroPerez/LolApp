package com.example.lolapp.Data.Local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SkinDao {

    @Query("SELECT * FROM skins WHERE championId = :championId")
    suspend fun getSkinsForChampion(championId: String): List<SkinEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(skins: List<SkinEntity>)

    @Query("DELETE FROM skins WHERE championId = :championId")
    suspend fun deleteSkinsForChampion(championId: String)
}
