package com.example.lolapp.Activities.Champions.DetailChampions.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lolapp.Adapters.SkinsAdapter
import com.example.lolapp.Data.Repository.ChampionRepository
import com.example.lolapp.Data.Skins
import com.example.lolapp.databinding.FragmentSkinsBinding
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SkinsFragment : Fragment() {

    private var _binding: FragmentSkinsBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: ChampionRepository
    private var championId: String? = null
    private var skins: List<Skins> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSkinsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        championId = arguments?.getString("champion_id") ?: return
        repository = createRepository()

        binding.rvSkins.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        lifecycleScope.launch {
            try {
                val champion = repository.getChampionDetail(championId!!)
                skins = champion.skins
                binding.rvSkins.adapter = SkinsAdapter(championId!!, skins)
            } catch (e: Exception) {
                e.printStackTrace()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
