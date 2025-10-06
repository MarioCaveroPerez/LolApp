package com.example.lolapp.Data

import com.google.gson.annotations.SerializedName

data class Champion(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("title") val title: String,
    @SerializedName("image") val image: ChampionImageResponse
)

data class ChampionImageResponse(
    @SerializedName("full") val url: String
)