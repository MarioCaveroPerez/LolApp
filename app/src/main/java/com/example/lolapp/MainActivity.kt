package com.example.lolapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lolapp.Adapters.ChampionAdapter
import com.example.lolapp.Utils.ApiService
import com.example.lolapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

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

        binding.rvLolChampsList.layoutManager = GridLayoutManager(this, 4)

        loadChampions()
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.btnSearch.setOnClickListener {

            binding.ivSearch.visibility = View.GONE
            binding.btnMenu.visibility = View.GONE
            binding.ivMenu.visibility = View.GONE
            binding.tvTitle.visibility = View.GONE
            binding.btnSearch.visibility = View.GONE

            binding.sbvChampsLol.visibility = View.VISIBLE


            val editText = binding.sbvChampsLol.findViewById<EditText>(com.ignite.material.searchbarview.R.id.editTextSearch)
            editText.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun loadChampions() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getChampions()
                val championList = response.data.values.toList()

                withContext(Dispatchers.Main){
                    adapter = ChampionAdapter(championList){ id ->
                        Toast.makeText(this@MainActivity, "Has pulsado $id", Toast.LENGTH_SHORT).show()
                    }
                    binding.rvLolChampsList.adapter = adapter
                }
            } catch (e : Exception) {
                withContext(Dispatchers.Main){
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://ddragon.leagueoflegends.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}