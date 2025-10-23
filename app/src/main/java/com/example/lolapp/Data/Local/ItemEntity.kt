package com.example.lolapp.Data.Local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val name: String, // usamos name como PK por simplicidad
    val description: String,
    val goldBase: Int,
    val goldTotal: Int,
    val goldSell: Int,
    val purchasable: Boolean,
    val map11: Boolean,
    val imageFull: String,
    val into: List<String>? = null,
    val from: List<String>? = null
)
