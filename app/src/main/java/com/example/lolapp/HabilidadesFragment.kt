package com.example.lolapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lolapp.Adapters.SpellsAdapter
import com.example.lolapp.Data.SpellItem
import com.example.lolapp.Data.SpellApi
import com.example.lolapp.Data.ChampionWithSpells
import com.example.lolapp.Data.ChampionDetailWithSpellsResponse
import com.example.lolapp.Utils.ApiService
import com.example.lolapp.databinding.FragmentHabilidadesBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HabilidadesFragment : Fragment() {

    private var _binding: FragmentHabilidadesBinding? = null
    private val binding get() = _binding!!
    private lateinit var spellsAdapter: SpellsAdapter

    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://ddragon.leagueoflegends.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabilidadesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val championId = arguments?.getString("champion_id") ?: return

        spellsAdapter = SpellsAdapter(listOf())
        binding.rvSpells.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = spellsAdapter
        }

        fetchChampionSpells(championId)
    }

    private fun fetchChampionSpells(championId: String) {
        lifecycleScope.launch {
            try {
                val response: ChampionDetailWithSpellsResponse = withContext(Dispatchers.IO) {
                    apiService.getChampionDetails(championId)
                }

                val champion: ChampionWithSpells = response.data[championId]!!

                val spells: List<SpellItem> = champion.spells.map { spellApi: SpellApi ->
                    val descriptionClean = spellApi.description.replace("<br>", "\n")
                    val costClean = if (spellApi.cost.all { it == "0" }) "Sin Coste" else spellApi.cost.joinToString(", ")

                    SpellItem(
                        id = spellApi.id,
                        name = spellApi.name,
                        description = descriptionClean,
                        cost = costClean,
                        image = spellApi.image
                    )
                }

                spellsAdapter = SpellsAdapter(spells)
                binding.rvSpells.adapter = spellsAdapter

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
