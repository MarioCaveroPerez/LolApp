package com.example.lolapp.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.lolapp.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var switchTheme: Switch
    private lateinit var spinnerLanguage: Spinner
    private lateinit var tvAppVersion: TextView
    private lateinit var btnRateApp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        switchTheme = findViewById(R.id.switchTheme)
        spinnerLanguage = findViewById(R.id.spinnerLanguage)
        tvAppVersion = findViewById(R.id.tvAppVersion)
        btnRateApp = findViewById(R.id.btnRateApp)

        // Configurar versión de la app
        val versionName = packageManager.getPackageInfo(packageName, 0).versionName
        tvAppVersion.text = versionName

        // Configurar spinner de idioma
        val languages = resources.getStringArray(R.array.languages)
        spinnerLanguage.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languages)

        // Switch de tema (solo ejemplo, no aplica cambios aquí)
        switchTheme.isChecked = false // Light por defecto
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            // Aquí puedes cambiar tema de la app
        }

        // Botón de calificar
        btnRateApp.setOnClickListener {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/MarioCvPz")
                    )
                )
            } catch (e: Exception) {}
        }
    }
}
