package com.example.lolapp.Utils

import com.example.lolapp.Data.Champion
import com.example.lolapp.Data.ChampionsResponse
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {



    @GET("cdn/15.19.1/data/es_ES/champion.json")
    suspend fun getChampions(): ChampionsResponse

}