package com.example.lolapp.Data.Local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ChampionEntity::class, ChampionDetailEntity::class, ChampionSpellsEntity::class, SkinEntity::class],
    version = 5,
    exportSchema = false
)
abstract class LoLDatabase : RoomDatabase() {
    abstract fun championDao(): ChampionDao
    abstract fun championDetailDao(): ChampionDetailDao
    abstract fun championSpellsDao(): ChampionSpellsDao
    abstract fun skinDao(): SkinDao
}
