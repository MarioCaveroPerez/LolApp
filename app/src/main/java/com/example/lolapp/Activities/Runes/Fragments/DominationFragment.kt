package com.example.lolapp.Activities.Runes.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lolapp.R
import com.example.lolapp.Utils.ApiService
import com.example.lolapp.Data.RuneStyle
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DominationFragment : Fragment() {

    private lateinit var keyRunesViews: List<ShapeableImageView>
    private lateinit var subRuneRows: List<List<ShapeableImageView>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_domination, container, false)

        // Referencias a las imágenes principales (key runes)
        keyRunesViews = listOf(
            view.findViewById(R.id.image1),
            view.findViewById(R.id.image2),
            view.findViewById(R.id.image3)
        )

        // Referencias a las imágenes de subrunas (3 filas x 3)
        val llSubContainer = view.findViewById<ViewGroup>(R.id.llSubRunesContainer)
        subRuneRows = (0 until llSubContainer.childCount).map { rowIndex ->
            val row = llSubContainer.getChildAt(rowIndex) as ViewGroup
            (0 until row.childCount).map { i -> row.getChildAt(i) as ShapeableImageView }
        }

        loadDominationRunes(view)

        return view
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://ddragon.leagueoflegends.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun loadDominationRunes(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = getRetrofit().create(ApiService::class.java)
                val runes = api.getRunes()

                val domination = runes.find { it.key.equals("Domination", ignoreCase = true) }
                domination?.let { runeStyle ->
                    withContext(Dispatchers.Main) {
                        showRunes(runeStyle)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showRunes(runeStyle: RuneStyle) {
        val baseUrl = "https://ddragon.leagueoflegends.com/cdn/img/"

        // Mostrar Key runes (primer slot)
        runeStyle.slots.firstOrNull()?.runes?.take(3)?.forEachIndexed { index, rune ->
            if (index < keyRunesViews.size) {
                val fullUrl = baseUrl + rune.icon
                val imageView = keyRunesViews[index]

                Picasso.get().load(baseUrl + rune.icon).into(keyRunesViews[index])

                imageView.setOnClickListener {
                    RuneDetailDialogFragment.newInstance(
                        rune.name,
                        rune.longDesc!!,
                        fullUrl
                    ).show(parentFragmentManager, "rune_detail")
                }
            }
        }

        // Mostrar subrunas (siguientes slots)
        runeStyle.slots.drop(1).take(3).forEachIndexed { rowIndex, slot ->
            if (rowIndex < subRuneRows.size) {
                slot.runes.take(3).forEachIndexed { i, rune ->
                    if (i < subRuneRows[rowIndex].size) {
                        val fullUrl = baseUrl + rune.icon
                        val imageView = subRuneRows[rowIndex][i]

                        Picasso.get().load(baseUrl + rune.icon).into(subRuneRows[rowIndex][i])

                        imageView.setOnClickListener {
                            RuneDetailDialogFragment.newInstance(
                                rune.name,
                                rune.longDesc!!,
                                fullUrl
                            ).show(parentFragmentManager, "rune_detail")
                        }
                    }
                }
            }
        }
    }
}