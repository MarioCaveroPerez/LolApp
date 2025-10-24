package com.example.lolapp.Data.Local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "runes")
data class RuneEntity(
    @PrimaryKey val key: String,            // "Domination", "Precision", etc.
    val runeNames: List<String>,            // Lista con los nombres de las runas
    val runeLongDescs: List<String>,        // Lista con las descripciones largas
    val slots: List<String>                  // Lista con URLs de los iconos
)
