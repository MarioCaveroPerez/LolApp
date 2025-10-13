package com.example.lolapp.Activities

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
import com.example.lolapp.Adapters.ChampionAdapter
import com.example.lolapp.Adapters.ItemAdapter
import com.example.lolapp.Data.Champion
import com.example.lolapp.Data.Item
import com.example.lolapp.Data.ItemsResponse
import com.example.lolapp.R
import com.example.lolapp.Utils.ApiService
import com.example.lolapp.databinding.ActivityItemsBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ItemsActivity : AppCompatActivity() {

        private lateinit var allItems: List<Item>
        private lateinit var drawerLayout: DrawerLayout
        private lateinit var navView: NavigationView

        private lateinit var binding: ActivityItemsBinding

        private lateinit var adapter: ItemAdapter

        private lateinit var retrofit: Retrofit
        private lateinit var apiService: ApiService

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            binding = ActivityItemsBinding.inflate(layoutInflater)
            setContentView(binding.root)

            retrofit = getRetrofit()
            apiService = retrofit.create(ApiService::class.java)

            drawerLayout = binding.drawerLayout
            navView = binding.navView

            navView.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.nav_champ -> {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    R.id.nav_items -> {}
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

            binding.rvitemslol.layoutManager = GridLayoutManager(this, 4)

            loadItems()
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

                binding.sbvitemslol.visibility = View.VISIBLE


                val editText =
                    binding.sbvitemslol.findViewById<EditText>(com.ignite.material.searchbarview.R.id.editTextSearch)
                editText.requestFocus()
                editText.doOnTextChanged { text, _, _, _ ->
                    val query = normalizeText(text.toString())
                    val filteredList = allItems.filter {
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
                        binding.sbvitemslol.visibility == View.VISIBLE -> {
                            binding.sbvitemslol.visibility = View.GONE
                            binding.ivSearch.visibility = View.VISIBLE
                            binding.btnMenu.visibility = View.VISIBLE
                            binding.ivMenu.visibility = View.VISIBLE
                            binding.tvTitle.visibility = View.VISIBLE
                            binding.btnSearch.visibility = View.VISIBLE

                            val editText = binding.sbvitemslol.findViewById<EditText>(
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

        private fun loadItems() {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = apiService.getItems()
                    val itemList = response.data.values.toList()

                    withContext(Dispatchers.Main) {
                        allItems = itemList
                        adapter = ItemAdapter(allItems) { itemName ->
                            val intent = Intent(this@ItemsActivity, DetailChampionsActivity::class.java)
                            intent.putExtra("item_name", itemName)
                            startActivity(intent)
                        }
                        binding.rvitemslol.adapter = adapter
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ItemsActivity, "Error: ${e.message}", Toast.LENGTH_LONG)
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