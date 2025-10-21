package com.example.lolapp.Activities.Champions.DetailChampions.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.lolapp.Data.ChampionDetailFull
import com.example.lolapp.Data.ChampionStatsUI
import com.example.lolapp.Data.Repository.ChampionRepository
import com.example.lolapp.databinding.FragmentGeneralBinding
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GeneralFragment : Fragment() {

    private var _binding: FragmentGeneralBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: ChampionRepository
    private var championId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGeneralBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        championId = arguments?.getString("champion_id")
        repository = createRepository()
        championId?.let { fetchChampionStats(it) }
    }

    private fun fetchChampionStats(championId: String) {
        lifecycleScope.launch {
            try {
                val champion: ChampionDetailFull = repository.getChampionDetail(championId)
                val stats = ChampionStatsUI(
                    hp = champion.stats.hp,
                    hpPerLevel = champion.stats.hpperlevel,
                    mp = champion.stats.mp,
                    mpPerLevel = champion.stats.mpperlevel,
                    attackDamage = champion.stats.attackdamage,
                    attackDamagePerLevel = champion.stats.attackdamageperlevel,
                    attackSpeed = champion.stats.attackspeed.toFloat(),
                    attackSpeedPerLevel = champion.stats.attackspeedperlevel.toFloat(),
                    armor = champion.stats.armor.toInt(),
                    armorPerLevel = champion.stats.armorperlevel.toFloat(),
                    spellBlock = champion.stats.spellblock.toInt(),
                    spellBlockPerLevel = champion.stats.spellblockperlevel.toFloat(),
                    attackRange = champion.stats.attackrange,
                    moveSpeed = champion.stats.movespeed,
                    attack = champion.info.attack,
                    defense = champion.info.defense,
                    magic = champion.info.magic,
                    difficulty = champion.info.difficulty
                )
                displayStats(stats)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun displayStats(stats: ChampionStatsUI) {
        binding.tvHpValue.text = "${stats.hp} - ${stats.hp + stats.hpPerLevel * 17}"
        binding.tvMpValue.text = "${stats.mp} - ${stats.mp + stats.mpPerLevel * 17}"
        binding.tvAttackDamageValue.text = "${stats.attackDamage} - ${stats.attackDamage + stats.attackDamagePerLevel * 17}"

        val attackSpeed = stats.attackSpeed * (1 + stats.attackSpeedPerLevel * 17 / 100)
        binding.tvAttackSpeedValue.text = String.format("%.2f", attackSpeed)

        binding.tvArmorValue.text = "${stats.armor} - ${String.format("%.2f", stats.armor + stats.armorPerLevel * 17)}"
        binding.tvMagicResistValue.text = "${stats.spellBlock} - ${String.format("%.2f", stats.spellBlock + stats.spellBlockPerLevel * 17)}"
        binding.tvRangeValue.text = stats.attackRange.toString()
        binding.tvMoveSpeedValue.text = stats.moveSpeed.toString()
        binding.pbAttack.progress = stats.attack
        binding.pbDefense.progress = stats.defense
        binding.pbMagic.progress = stats.magic
        binding.pbDifficulty.progress = stats.difficulty
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
