package com.example.lolapp.Activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lolapp.Data.Skins
import com.example.lolapp.Fragments.GeneralFragment
import com.example.lolapp.Fragments.HabilidadesFragment
import com.example.lolapp.Fragments.LoreFragment
import com.example.lolapp.R
import com.example.lolapp.Fragments.SkinsFragment
import com.example.lolapp.Utils.ApiService
import com.example.lolapp.databinding.ActivityDetailChampionsBinding
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailChampionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailChampionsBinding
    private lateinit var retrofit: Retrofit
    private lateinit var apiService: ApiService

    private var currentChampionId: String? = null
    private var currentChampionSkins = emptyList<Skins>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailChampionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retrofit = getRetrofit()
        apiService = retrofit.create(ApiService::class.java)

        val championId = intent.getStringExtra("champion_id")
        if (championId != null) {
            loadChampionDetail(championId)
        } else {
            Toast.makeText(this, "No se recibió el campeón", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadChampionDetail(championId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getDetailChampions()
                val champion = response.data[championId]!!

                withContext(Dispatchers.Main) {
                    currentChampionId = champion.id
                    currentChampionSkins = champion.skins

                    binding.tvChampionName.text = champion.name
                    binding.tvChampionTitle.text = champion.title

                    val splashUrl =
                        "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/${champion.id}_0.jpg"
                    Picasso.get().load(splashUrl).fit().centerCrop().into(binding.ivSplashArt)

                    // Cargar el fragment inicial con champion_id
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fragmentContainerChampion,
                            GeneralFragment().apply {
                                arguments = Bundle().apply {
                                    putString("champion_id", currentChampionId)
                                }
                            }
                        )
                        .commit()

                    setupTabs()
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@DetailChampionsActivity,
                        "Error al cargar datos: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun setupTabs() {
        val tabs = listOf("General", "Habilidades", "Skins", "Lore")
        for (tabName in tabs) {
            binding.tabLayoutChampion.addTab(binding.tabLayoutChampion.newTab().setText(tabName))
        }

        binding.tabLayoutChampion.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val fragment = when (tab?.position) {
                    0 -> GeneralFragment().apply {
                        arguments = Bundle().apply { putString("champion_id", currentChampionId) }
                    }
                    1 -> HabilidadesFragment().apply {
                        arguments = Bundle().apply { putString("champion_id", currentChampionId) }
                    }
                    2 -> SkinsFragment().apply {
                        arguments = Bundle().apply {
                            putString("champion_id", currentChampionId)
                            putParcelableArrayList("skins_list", ArrayList(currentChampionSkins))
                        }
                    }
                    3 -> LoreFragment().apply {
                        arguments = Bundle().apply { putString("champion_id", currentChampionId) }
                    }
                    else -> GeneralFragment().apply {
                        arguments = Bundle().apply { putString("champion_id", currentChampionId) }
                    }
                }

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerChampion, fragment)
                    .commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://ddragon.leagueoflegends.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
