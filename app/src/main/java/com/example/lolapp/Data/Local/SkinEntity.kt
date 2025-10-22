package com.example.lolapp.Data.Local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "skins")
data class SkinEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val championId: String,
    val num: Int,
    val name: String,
    val chromas: Boolean
)
