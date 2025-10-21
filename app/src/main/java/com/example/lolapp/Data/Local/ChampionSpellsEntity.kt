package com.example.lolapp.Data.Local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "champion_spells")
data class ChampionSpellsEntity(
    @PrimaryKey val id: String,
    val passiveName: String,
    val passiveDescription: String,
    val passiveImage: String,
    val spellsJson: String // Lista de habilidades en JSON
)
