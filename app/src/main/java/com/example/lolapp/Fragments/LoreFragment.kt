package com.example.lolapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.lolapp.Data.ChampionDetail
import com.example.lolapp.R
import com.example.lolapp.Utils.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoreFragment : Fragment() {

    private var championId: String? = null
    private lateinit var tvLore: TextView
    private val apiService by lazy {
        getRetrofit().create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        championId = arguments?.getString("champion_id")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lore, container, false)
        tvLore = view.findViewById(R.id.tvLoreText)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        championId?.let { fetchLore(it) }
    }

    private fun fetchLore(championId: String) {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getDetailChampions()
                }
                val champion: ChampionDetail = response.data[championId]!!
                tvLore.text = champion.lore
            } catch (e: Exception) {
                e.printStackTrace()
                tvLore.text = "No se pudo cargar el lore."
            }
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://ddragon.leagueoflegends.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
