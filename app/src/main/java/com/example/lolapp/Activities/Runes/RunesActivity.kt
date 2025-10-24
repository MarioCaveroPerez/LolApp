package com.example.lolapp.Activities.Runes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import com.example.lolapp.Activities.Champions.MainActivity
import com.example.lolapp.Activities.Info.InfoActivity
import com.example.lolapp.Activities.Items.ItemsActivity
import com.example.lolapp.Activities.Runes.Fragments.DominationFragment
import com.example.lolapp.Activities.Runes.Fragments.InspirationFragment
import com.example.lolapp.Activities.Runes.Fragments.PrecisionFragment
import com.example.lolapp.Activities.Runes.Fragments.SorceryFragment
import com.example.lolapp.Activities.Runes.Fragments.ValorFragment
import com.example.lolapp.Activities.Settings.SettingsActivity
import com.example.lolapp.R
import com.example.lolapp.databinding.ActivityRunesBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout


class RunesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRunesBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRunesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        navView = binding.navView

        setupNavigation()
        setupToolbar()
        setupBackPressHandler()
        setupTabs()
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
    }

    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when {
                    drawerLayout.isDrawerOpen(GravityCompat.START) -> drawerLayout.closeDrawer(GravityCompat.START)
                    binding.sbvChampsLol.isVisible -> {
                        binding.sbvChampsLol.visibility = View.GONE
                        binding.btnMenu.visibility = View.VISIBLE
                        binding.ivMenu.visibility = View.VISIBLE
                        binding.tvTitle.visibility = View.VISIBLE

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
        val tabs = listOf("DOMI", "PRECI", "SORCE", "VALOR", "INSP")
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
                    .replace(R.id.fragmentContainerRunes, fragment)
                    .commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        val defaultTabIndex = 0
        binding.tabLayoutRunes.getTabAt(defaultTabIndex)?.select()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerRunes, DominationFragment())
            .commit()
    }
}
