package com.example.lolapp.Data

data class ItemsResponse(
    val data: Map<String, Item>
)

data class Item(
    val name: String,
    val description: String,
    val gold: Gold,
    val image: ItemImage,
    val purchasable: Boolean = true,
    val maps: Map<String, Boolean> = mapOf(),
    val into: List<String>? = null,
    val from: List<String>? = null
)

data class Gold(
    val base: Int,
    val total: Int,
    val sell: Int,
    val purchasable: Boolean? = true
)

data class ItemImage(
    val full: String
)
