package com.example.lolapp.Data

data class SpellItem(
    val id: String,
    val name: String,
    val description: String,
    val cost: String,
    val image: SpellImage
)

data class SpellImage(
    val full: String
)

// Clase que mapea la respuesta de la API
data class ChampionWithSpells(
    val id: String,
    val name: String,
    val passive: SpellApi,
    val spells: List<SpellApi>
)

data class SpellApi(
    val id: String? = null,
    val name: String,
    val description: String,
    val cost: List<String> = listOf(),
    val image: SpellImage
)

data class ChampionDetailWithSpellsResponse(
    val data: Map<String, ChampionWithSpells>
)
