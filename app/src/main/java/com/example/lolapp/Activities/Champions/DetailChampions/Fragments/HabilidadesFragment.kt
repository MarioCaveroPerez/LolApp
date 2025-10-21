package com.example.lolapp.Activities.Champions.DetailChampions.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lolapp.Adapters.SpellsAdapter
import com.example.lolapp.Data.SpellItem
import com.example.lolapp.Data.Repository.ChampionRepository
import com.example.lolapp.databinding.FragmentHabilidadesBinding
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HabilidadesFragment : Fragment() {

    private var _binding: FragmentHabilidadesBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: ChampionRepository
    private lateinit var spellsAdapter: SpellsAdapter

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
        repository = createRepository()

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
                val champion = repository.getChampionSpells(championId)
                val spells: List<SpellItem> = listOf(
                    SpellItem(
                        id = champion.passive.id ?: "passive",
                        name = champion.passive.name,
                        description = cleanDescription(champion.passive.description),
                        cost = "Sin Coste",
                        image = champion.passive.image
                    )
                ) + champion.spells.map { spell ->
                    val descriptionClean = cleanDescription(spell.description)
                    val costClean = if (spell.cost.all { it == "0" }) "Sin Coste" else spell.cost.joinToString(", ")
                    SpellItem(
                        id = spell.id ?: "",
                        name = spell.name,
                        description = descriptionClean,
                        cost = costClean,
                        image = spell.image
                    )
                }
                spellsAdapter.updateList(spells)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun cleanDescription(description: String): String {
        var text = description.replace("<br>", "\n", ignoreCase = true)
        text = text.replace(Regex("<.*?>"), "")
        return text
    }

    private fun createRepository(): ChampionRepository {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ddragon.leagueoflegends.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(com.example.lolapp.Utils.ApiService::class.java)
        return ChampionRepository.create(requireContext(), apiService)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
