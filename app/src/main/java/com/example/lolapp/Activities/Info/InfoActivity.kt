package com.example.lolapp.Activities.Info

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lolapp.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}