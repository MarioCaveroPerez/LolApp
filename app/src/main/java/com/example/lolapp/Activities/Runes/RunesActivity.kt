package com.example.lolapp.Activities.Runes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lolapp.Activities.Champions.DetailChampions.DetailChampionsActivity
import com.example.lolapp.Activities.Champions.DetailChampions.Fragments.GeneralFragment
import com.example.lolapp.Activities.Champions.DetailChampions.Fragments.HabilidadesFragment
import com.example.lolapp.Activities.Champions.DetailChampions.Fragments.LoreFragment
import com.example.lolapp.Activities.Champions.DetailChampions.Fragments.SkinsFragment
import com.example.lolapp.Activities.Champions.MainActivity
import com.example.lolapp.Activities.Info.InfoActivity
import com.example.lolapp.Activities.Items.ItemsActivity
import com.example.lolapp.Activities.Runes.Fragments.DominationFragment
import com.example.lolapp.Activities.Runes.Fragments.InspirationFragment
import com.example.lolapp.Activities.Runes.Fragments.PrecisionFragment
import com.example.lolapp.Activities.Runes.Fragments.SorceryFragment
import com.example.lolapp.Activities.Runes.Fragments.ValorFragment
import com.example.lolapp.Activities.Settings.SettingsActivity
import com.example.lolapp.Adapters.ChampionAdapter
import com.example.lolapp.Data.Champion
import com.example.lolapp.Data.ChampionImageResponse
import com.example.lolapp.Data.Mappers.toEntity
import com.example.lolapp.Data.Repository.ChampionRepository
import com.example.lolapp.R
import com.example.lolapp.Utils.ApiService
import com.example.lolapp.databinding.ActivityMainBinding
import com.example.lolapp.databinding.ActivityRunesBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RunesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRunesBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var adapter: ChampionAdapter
    private lateinit var allChampions: List<Champion>
    private lateinit var repository: ChampionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRunesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        navView = binding.navView

        repository = createRepository()

        setupNavigation()
        setupToolbar()
        setupBackPressHandler()
        setupTabs()
        setupRecyclerView()
        loadChampions()
    }

    private fun setupNavigation() {
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_champ ->startActivity(Intent(this, MainActivity::class.java))
                R.id.nav_items -> startActivity(Intent(this, ItemsActivity::class.java))
                R.id.nav_settings -> startActivity(Intent(this, SettingsActivity::class.java))
                R.id.info -> startActivity(Intent(this, InfoActivity::class.java))
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun setupToolbar() {
        binding.btnMenu.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
        binding.btnSearch.setOnClickListener {
            binding.ivSearch.visibility = View.GONE
            binding.btnMenu.visibility = View.GONE
            binding.ivMenu.visibility = View.GONE
            binding.tvTitle.visibility = View.GONE
            binding.btnSearch.visibility = View.GONE
            binding.sbvChampsLol.visibility = View.VISIBLE

            val editText =
                binding.sbvChampsLol.findViewById<EditText>(com.ignite.material.searchbarview.R.id.editTextSearch)
            editText.requestFocus()
            editText.doOnTextChanged { text, _, _, _ ->
                val query = normalizeText(text.toString())
                val filteredList = allChampions.filter {
                    normalizeText(it.name).contains(query)
                }
                adapter.updateList(filteredList)
            }

            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when {
                    drawerLayout.isDrawerOpen(GravityCompat.START) -> drawerLayout.closeDrawer(GravityCompat.START)
                    binding.sbvChampsLol.isVisible -> {
                        binding.sbvChampsLol.visibility = View.GONE
                        binding.ivSearch.visibility = View.VISIBLE
                        binding.btnMenu.visibility = View.VISIBLE
                        binding.ivMenu.visibility = View.VISIBLE
                        binding.tvTitle.visibility = View.VISIBLE
                        binding.btnSearch.visibility = View.VISIBLE

                        val editText =
                            binding.sbvChampsLol.findViewById<EditText>(com.ignite.material.searchbarview.R.id.editTextSearch)
                        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(editText.windowToken, 0)
                    }
                    else -> {
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        })
    }

    private fun setupTabs() {
        val tabs = listOf("Dominacion", "Precision", "Brujeria", "Valor", "Inspiracion")
        for (tabName in tabs) {
            binding.tabLayoutRunes.addTab(binding.tabLayoutRunes.newTab().setText(tabName))
        }

        binding.tabLayoutRunes.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val fragment = when (tab?.position) {
                    0 -> DominationFragment()
                    1 -> PrecisionFragment()
                    2 -> SorceryFragment()
                    3 -> ValorFragment()
                    4 -> InspirationFragment()
                    else -> DominationFragment()
                }

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerChampion, fragment)
                    .commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupRecyclerView() {
        binding.rvRunesLol.layoutManager = GridLayoutManager(this, 4)
    }

    private fun loadChampions() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val championEntities = repository.getChampions()
                val championList = championEntities.map { entity ->
                    Champion(
                        id = entity.id,
                        name = entity.name,
                        title = entity.title,
                        image = ChampionImageResponse(full = entity.image.full)
                    )
                }
                withContext(Dispatchers.Main) {
                    allChampions = championList
                    adapter = ChampionAdapter(allChampions) { championId ->
                        val intent = Intent(this@RunesActivity, DetailChampionsActivity::class.java)
                        intent.putExtra("champion_id", championId)
                        startActivity(intent)
                    }
                    binding.rvRunesLol.adapter = adapter
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RunesActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun createRepository(): ChampionRepository {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ddragon.leagueoflegends.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiService::class.java)
        return ChampionRepository.create(this, apiService)
    }

    private fun normalizeText(text: String): String {
        return text.lowercase()
            .replace("'", "")
            .replace("\\s".toRegex(), "")
            .replace("[^\\p{ASCII}]".toRegex(), "")
    }
}
