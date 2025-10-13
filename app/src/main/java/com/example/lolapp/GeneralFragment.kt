package com.example.lolapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lolapp.Data.ChampionDetailWithStatsResponse
import com.example.lolapp.Data.ChampionStatsUI
import com.example.lolapp.Utils.ApiService
import com.example.lolapp.databinding.FragmentGeneralBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GeneralFragment : Fragment() {

    private var _binding: FragmentGeneralBinding? = null
    private val binding get() = _binding!!

    private lateinit var apiService: ApiService
    private var championId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGeneralBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        championId = arguments?.getString("champion_id")

        val retrofit = Retrofit.Builder()
            .baseUrl("https://ddragon.leagueoflegends.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        championId?.let { fetchChampionStats(it) }
    }

    private fun fetchChampionStats(championId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: ChampionDetailWithStatsResponse =
                    apiService.getChampionDetailsWithStats(championId)

                val champion = response.data[championId]!!

                // Mapear a ChampionStatsUI
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

                withContext(Dispatchers.Main) {
                    displayStats(stats)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun displayStats(stats: ChampionStatsUI) {
        // Formatear valores con 2 decimales donde sea necesario
        binding.tvHpValue.text = "${stats.hp} - ${stats.hp + stats.hpPerLevel * 17}"
        binding.tvMpValue.text = "${stats.mp} - ${stats.mp + stats.mpPerLevel * 17}"
        binding.tvAttackDamageValue.text =
            "${stats.attackDamage} - ${stats.attackDamage + stats.attackDamagePerLevel * 17}"

        // Attack Speed con 2 decimales
        val attackSpeed = stats.attackSpeed * (1 + stats.attackSpeedPerLevel * 17 / 100)
        binding.tvAttackSpeedValue.text = String.format("%.2f", attackSpeed)

        // Armor y SpellBlock con 2 decimales
        val armorMax = stats.armor + stats.armorPerLevel * 17
        binding.tvArmorValue.text = "${stats.armor} - ${String.format("%.2f", armorMax)}"

        val spellBlockMax = stats.spellBlock + stats.spellBlockPerLevel * 17
        binding.tvMagicResistValue.text = "${stats.spellBlock} - ${String.format("%.2f", spellBlockMax)}"

        // Valores enteros
        binding.tvRangeValue.text = stats.attackRange.toString()
        binding.tvMoveSpeedValue.text = stats.moveSpeed.toString()

        // ProgressBars (valores entre 0-10)
        binding.pbAttack.progress = stats.attack
        binding.pbDefense.progress = stats.defense
        binding.pbMagic.progress = stats.magic
        binding.pbDifficulty.progress = stats.difficulty
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
