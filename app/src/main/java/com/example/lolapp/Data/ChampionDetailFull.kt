package com.example.lolapp.Data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ChampionDetailFull(
    val id: String,
    val name: String,
    val title: String,
    val stats: Stats,
    val info: Info
)

data class Stats(
    val hp: Double,
    val hpperlevel: Double,
    val mp: Double,
    val mpperlevel: Double,
    val movespeed: Double,
    val armor: Double,
    val armorperlevel: Double,
    val spellblock: Double,
    val spellblockperlevel: Double,
    val attackrange: Double,
    val attackdamage: Double,
    val attackdamageperlevel: Double,
    val attackspeed: Double,
    val attackspeedperlevel: Double
)

data class Info(
    val attack: Int,
    val defense: Int,
    val magic: Int,
    val difficulty: Int
)

// Wrapper de respuesta de la API para stats
data class ChampionDetailWithStatsResponse(
    val data: Map<String, ChampionDetailFull>
)

// Clase UI para mostrar en fragment
@Parcelize

data class ChampionStatsUI(
    val hp: Double,
    val hpPerLevel: Double,
    val mp: Double,
    val mpPerLevel: Double,
    val attackDamage: Double,
    val attackDamagePerLevel: Double,
    val attackSpeed: Float,
    val attackSpeedPerLevel: Float,
    val armor: Int,
    val armorPerLevel: Float,
    val spellBlock: Int,
    val spellBlockPerLevel: Float,
    val attackRange: Double,
    val moveSpeed: Double,
    val attack: Int,
    val defense: Int,
    val magic: Int,
    val difficulty: Int
) : Parcelable