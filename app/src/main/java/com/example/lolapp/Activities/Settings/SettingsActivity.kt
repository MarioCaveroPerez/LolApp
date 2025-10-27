package com.example.lolapp.Activities.Settings

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.lolapp.R
import com.example.lolapp.databinding.ActivityDetailChampionsBinding
import com.example.lolapp.databinding.ActivitySettingsBinding
import java.util.Locale

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var spinnerLanguage: Spinner
    private lateinit var btnRateApp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val lang = prefs.getString("language", "es") ?: "es"
        setLocale(lang)

        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinnerLanguage = binding.spinnerLanguage
        btnRateApp = binding.btnRateApp

        // Configurar spinner de idioma
        val languages = resources.getStringArray(R.array.languages)
        spinnerLanguage.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languages)


        spinnerLanguage.setSelection(
            when (lang) {
                "es" -> 0
                "en" -> 1
                "fr" -> 2
                else -> 1
            }
        )

        spinnerLanguage.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                val selectedLanguage = when (position) {
                    0 -> "es"
                    1 -> "en"
                    2 -> "fr"
                    else -> "en"
                }

                val currentLang = prefs.getString("language", "es")
                if (currentLang != selectedLanguage) {
                    setLocale(selectedLanguage)
                    recreate() // Recargar actividad con idioma actualizado
                }
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
        }

        // Botón de calificar
        btnRateApp.setOnClickListener {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/MarioCaveroPerez/LolApp.git")
                    )
                )
            } catch (e: Exception) {}
        }
        val button = binding.buttonBack
        button.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.edit().putString("language", languageCode).apply()
    }
}