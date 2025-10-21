package com.example.lolapp.Data.Local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "champions")
data class ChampionEntity(
    @PrimaryKey val id: String,
    val name: String,
    val title: String,
    val imageFull: String
)
