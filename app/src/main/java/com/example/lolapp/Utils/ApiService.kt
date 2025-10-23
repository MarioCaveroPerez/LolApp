package com.example.lolapp.Utils

import com.example.lolapp.Data.Champion
import com.example.lolapp.Data.ChampionDetailWithSpellsResponse
import com.example.lolapp.Data.ChampionDetailWithStatsResponse
import com.example.lolapp.Data.ChampionsResponse
import com.example.lolapp.Data.DetailChampionResponse
import com.example.lolapp.Data.ItemsResponse
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {


        @GET("cdn/15.21.1/data/es_ES/champion.json")
        suspend fun getChampions(): ChampionsResponse

        @GET("cdn/15.21.1/data/es_ES/item.json")
        suspend fun getItems(): ItemsResponse

        @GET("cdn/15.21.1/data/es_ES/championFull.json")
        suspend fun getDetailChampions(): DetailChampionResponse

        @GET("cdn/15.21.1/data/es_ES/champion/{champion}.json")
        suspend fun getChampionDetailsWithStats(@Path("champion") championId: String): ChampionDetailWithStatsResponse

        @GET("cdn/15.21.1/data/es_ES/champion/{champion}.json")
        suspend fun getChampionDetailsWithSpells(@Path("champion") championId: String): ChampionDetailWithSpellsResponse

}