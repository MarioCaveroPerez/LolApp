package com.example.lolapp.Data

data class ChampionDetailFull(
    val id: String,
    val name: String,
    val title: String,
    val stats: Stats,
    val info: Info
)

data class Stats(
    val hp: Int,
    val hpperlevel: Int,
    val mp: Int,
    val mpperlevel: Int,
    val movespeed: Int,
    val armor: Double,
    val armorperlevel: Double,
    val spellblock: Double,
    val spellblockperlevel: Double,
    val attackrange: Int,
    val attackdamage: Int,
    val attackdamageperlevel: Int,
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
data class ChampionStatsUI(
    val hp: Int,
    val hpPerLevel: Int,
    val mp: Int,
    val mpPerLevel: Int,
    val attackDamage: Int,
    val attackDamagePerLevel: Int,
    val attackSpeed: Float,
    val attackSpeedPerLevel: Float,
    val armor: Int,
    val armorPerLevel: Float,
    val spellBlock: Int,
    val spellBlockPerLevel: Float,
    val attackRange: Int,
    val moveSpeed: Int,
    val attack: Int,
    val defense: Int,
    val magic: Int,
    val difficulty: Int
)