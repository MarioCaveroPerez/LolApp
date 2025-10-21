package com.example.lolapp.Activities.Champions.DetailChampions.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.lolapp.Data.Repository.ChampionRepository
import com.example.lolapp.R
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoreFragment : Fragment() {

    private lateinit var tvLore: TextView
    private lateinit var repository: ChampionRepository
    private var championId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        championId = arguments?.getString("champion_id")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_lore, container, false)
        tvLore = view.findViewById(R.id.tvLoreText) // <- así obtenemos el TextView real
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = createRepository()
        championId?.let { fetchLore(it) }
    }

    private fun fetchLore(championId: String) {
        lifecycleScope.launch {
            try {
                val champion = repository.getChampionDetail(championId)
                tvLore.text = champion.lore
            } catch (e: Exception) {
                e.printStackTrace()
                tvLore.text = "No se pudo cargar el lore."
            }
        }
    }

    private fun createRepository(): ChampionRepository {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ddragon.leagueoflegends.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(com.example.lolapp.Utils.ApiService::class.java)
        return ChampionRepository.create(requireContext(), apiService)
    }
}
