package com.example.lolapp

import android.os.Bundle
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