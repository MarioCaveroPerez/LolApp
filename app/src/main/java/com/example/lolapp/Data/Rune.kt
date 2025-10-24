package com.example.lolapp.Data

data class RuneStyle(
    val id: Int,
    val key: String,
    val icon: String,
    val name: String,
    val slots: List<RuneSlot>
)

data class RuneSlot(
    val runes: List<Rune>
)

data class Rune(
    val id: Int,
    val key: String,
    val icon: String,
    val name: String,
    val shortDesc: String?,
    val longDesc: String?
)