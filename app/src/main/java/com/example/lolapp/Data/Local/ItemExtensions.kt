package com.example.lolapp.Data

import com.example.lolapp.Data.Local.ItemEntity

// Función de extensión para mapear de Item (API) a ItemEntity (Room)
fun Item.toEntity(): ItemEntity {
    return ItemEntity(
        name = this.name,
        description = this.description,
        goldBase = this.gold.base,
        goldTotal = this.gold.total,
        goldSell = this.gold.sell,
        purchasable = this.gold.purchasable ?: true,
        map11 = this.maps["11"] == true,
        imageFull = this.image.full,
        into = this.into,
        from = this.from
    )
}
