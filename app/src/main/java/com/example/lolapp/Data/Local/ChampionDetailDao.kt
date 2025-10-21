package com.example.lolapp.Data.Local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ChampionDetailDao {

    @Query("SELECT * FROM champion_details WHERE id = :id")
    suspend fun getChampionDetail(id: String): ChampionDetailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChampionDetail(champion: ChampionDetailEntity)
}
