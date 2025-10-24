package com.example.lolapp.Data.Local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [ChampionEntity::class, ChampionDetailEntity::class, ChampionSpellsEntity::class, SkinEntity::class, ItemEntity::class, RuneEntity::class],
    version = 8,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class LoLDatabase : RoomDatabase() {
    abstract fun championDao(): ChampionDao
    abstract fun championDetailDao(): ChampionDetailDao
    abstract fun championSpellsDao(): ChampionSpellsDao
    abstract fun skinDao(): SkinDao
    abstract fun itemDao(): ItemDao
    abstract fun runeDao(): RuneDao

}
