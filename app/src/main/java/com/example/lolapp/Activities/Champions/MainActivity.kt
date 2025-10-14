package com.example.lolapp.Activities.Champions

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.widget.doOnTextChanged
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lolapp.Activities.Champions.DetailChampions.DetailChampionsActivity
import com.example.lolapp.Activities.Info.InfoActivity
import com.example.lolapp.Activities.Items.ItemsActivity
import com.example.lolapp.Activities.Settings.SettingsActivity
import com.example.lolapp.Adapters.ChampionAdapter
import com.example.lolapp.Data.Champion
import com.example.lolapp.R
import com.example.lolapp.Utils.ApiService
import com.example.lolapp.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var allChampions: List<Champion>
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: ChampionAdapter

    private lateinit var retrofit: Retrofit
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retrofit = getRetrofit()
        apiService = retrofit.create(ApiService::class.java)

        drawerLayout = binding.drawerLayout
        navView = binding.navView

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_champ -> {}
                R.id.nav_items -> {
                    val intent = Intent(this, ItemsActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_runes -> {
                    Toast.makeText(this, "Runas", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                }
                R.id.info -> {
                    val intent = Intent(this, InfoActivity::class.java)
                    startActivity(intent)
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        binding.rvLolChampsList.layoutManager = GridLayoutManager(this, 4)

        loadChampions()
        setupToolbar()
        setupBackPressHandler()
    }

    private fun setupToolbar() {
        binding.btnMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
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

    fun normalizeText(text: String): String {
        return text
            .lowercase()
            .replace("'", "")
            .replace("\\s".toRegex(), "")
            .replace("[^\\p{ASCII}]".toRegex(), "")
    }

    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when {
                    drawerLayout.isDrawerOpen(GravityCompat.START) -> {
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }
                    binding.sbvChampsLol.visibility == View.VISIBLE -> {
                        binding.sbvChampsLol.visibility = View.GONE
                        binding.ivSearch.visibility = View.VISIBLE
                        binding.btnMenu.visibility = View.VISIBLE
                        binding.ivMenu.visibility = View.VISIBLE
                        binding.tvTitle.visibility = View.VISIBLE
                        binding.btnSearch.visibility = View.VISIBLE

                        val editText = binding.sbvChampsLol.findViewById<EditText>(
                            com.ignite.material.searchbarview.R.id.editTextSearch
                        )
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

    private fun loadChampions() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getChampions()
                val championList = response.data.values.toList()

                withContext(Dispatchers.Main) {
                    allChampions = championList
                    adapter = ChampionAdapter(allChampions) { championId ->
                        val intent = Intent(this@MainActivity, DetailChampionsActivity::class.java)
                        intent.putExtra("champion_id", championId)
                        startActivity(intent)
                    }
                    binding.rvLolChampsList.adapter = adapter
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://ddragon.leagueoflegends.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}