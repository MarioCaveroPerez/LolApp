package com.example.lolapp.Data

data class ItemsResponse(
    val data: Map<String, Item>
)

data class Item(
    val name: String,
    val description: String,
    val gold: Gold,
    val image: ItemImage
)

data class Gold(
    val base: Int,
    val total: Int,
    val sell: Int
)

data class ItemImage(
    val full: String
)
