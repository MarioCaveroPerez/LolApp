package com.example.lolapp.Data.Local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ChampionDao {

    @Query("SELECT * FROM champions")
    suspend fun getAllChampions(): List<ChampionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(champions: List<ChampionEntity>)

    @Query("DELETE FROM champions")
    suspend fun clearChampions()
}


@Dao
interface ChampionSpellsDao {

    @Query("SELECT * FROM champion_spells WHERE id = :championId")
    suspend fun getChampionSpells(championId: String): ChampionSpellsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChampionSpells(spells: ChampionSpellsEntity)
}
