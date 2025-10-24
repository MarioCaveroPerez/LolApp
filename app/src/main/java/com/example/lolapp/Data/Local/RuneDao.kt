package com.example.lolapp.Data.Local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RuneDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRune(rune: RuneEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRunes(runes: List<RuneEntity>)

    @Query("SELECT * FROM runes WHERE key = :key")
    suspend fun getRuneByKey(key: String): RuneEntity?

    @Query("SELECT * FROM runes")
    suspend fun getAllRunes(): List<RuneEntity>
}