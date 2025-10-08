package com.example.lolapp.Data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class DetailChampionResponse(
    @SerializedName("data") val data: Map<String, ChampionDetail>
)
data class ChampionDetail(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("title") val title: String,
    @SerializedName("skins") val skins: List<Skins>,
    @SerializedName("lore") val lore: String
)

@Parcelize
data class Skins(
    @SerializedName("num") val num: Int,
    @SerializedName("name") val name: String,
    @SerializedName("chromas") val chromas: Boolean
): Parcelable