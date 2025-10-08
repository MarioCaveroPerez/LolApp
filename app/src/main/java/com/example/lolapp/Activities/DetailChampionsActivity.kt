package com.example.lolapp.Activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentContainerView
import com.example.lolapp.GeneralFragment
import com.example.lolapp.HabilidadesFragment
import com.example.lolapp.LoreFragment
import com.example.lolapp.R
import com.example.lolapp.SkinsFragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailChampionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        retrofit = getRetrofit()
        apiService = retrofit.create(ApiService::class.java)

        val championId = intent.getStringExtra("champion_id")
        if (championId != null) {
            loadChampionDetail(championId)
        }

        setupTabs()
    }

    private fun loadChampionDetail(championId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getDetailChampions()
                val champion = response.data[championId]!!

                withContext(Dispatchers.Main) {
                    binding.tvChampionName.text = champion.name
                    binding.tvChampionTitle.text = champion.title

                    val splashUrl = "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/${champion.id}_0.jpg"
                    Picasso.get()
                        .load(splashUrl)
                        .fit()
                        .centerCrop()
                        .into(binding.ivSplashArt)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DetailChampionsActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupTabs() {
        val tabs = listOf("General", "Habilidades", "Skins", "Lore")
        for (tabName in tabs) {
            binding.tabLayoutChampion.addTab(binding.tabLayoutChampion.newTab().setText(tabName))
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerChampion, GeneralFragment())
            .commit()

        binding.tabLayoutChampion.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val fragment = when(tab?.position){
                    0 -> GeneralFragment()
                    1 -> HabilidadesFragment()
                    2 -> {
                        // Aquí pasamos la lista de skins al fragment
                        SkinsFragment().apply {
                            arguments = Bundle().apply {
                                putParcelableArrayList("skins_list", ArrayList(champion.skins))
                            }
                        }
                    }
                    3 -> LoreFragment()
                    else -> GeneralFragment()
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
        return Retrofit.Builder().baseUrl("https://ddragon.leagueoflegends.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}