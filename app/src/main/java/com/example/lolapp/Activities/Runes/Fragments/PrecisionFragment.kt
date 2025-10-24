package com.example.lolapp.Activities.Runes.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lolapp.Data.Local.DatabaseProvider
import com.example.lolapp.Data.Local.RuneEntity
import com.example.lolapp.Data.RuneStyle
import com.example.lolapp.R
import com.example.lolapp.Utils.ApiService
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.chunked
import kotlin.collections.forEachIndexed


class PrecisionFragment : Fragment() {
    private lateinit var keyRunesViews: List<ShapeableImageView>
    private lateinit var subRuneRows: List<List<ShapeableImageView>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_precision, container, false)

        // Referencias a las imágenes principales (key runes)
        keyRunesViews = listOf(
            view.findViewById(R.id.image1),
            view.findViewById(R.id.image2),
            view.findViewById(R.id.image3),
            view.findViewById(R.id.image4)
        )

        // Referencias a las imágenes de subrunas (3 filas x 3)
        val llSubContainer = view.findViewById<ViewGroup>(R.id.llSubRunesContainer)
        subRuneRows = (0 until llSubContainer.childCount).map { rowIndex ->
            val row = llSubContainer.getChildAt(rowIndex) as ViewGroup
            (0 until row.childCount).map { i -> row.getChildAt(i) as ShapeableImageView }
        }

        loadPrecisionRunes()

        return view
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://ddragon.leagueoflegends.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun loadPrecisionRunes() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = DatabaseProvider.getDatabase(requireContext())
            val dao = db.runeDao()

            var runeEntity: RuneEntity? = dao.getRuneByKey("Precision")

            if (runeEntity == null) {
                try {
                    val api = getRetrofit().create(ApiService::class.java)
                    val runes = api.getRunes()


                    val domination = runes.find { it.key.equals("Precision", ignoreCase = true) }

                    domination?.let {
                        val runeNames = it.slots.flatMap { slot -> slot.runes.map { r -> r.name } }
                        val runeLongDescs = it.slots.flatMap { slot -> slot.runes.map { r -> r.longDesc ?: "" } }
                        val iconUrls = it.slots.flatMap { slot -> slot.runes.map { r -> r.icon } }

                        runeEntity = RuneEntity(
                            key = it.key,
                            runeNames = runeNames,
                            runeLongDescs = runeLongDescs,
                            slots = iconUrls
                        )
                        dao.insertRune(runeEntity!!)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            runeEntity?.let {
                withContext(Dispatchers.Main) {
                    showRunesFromEntity(it)
                }
            }
        }
    }

    private fun showRunesFromEntity(entity: RuneEntity) {
        val baseUrl = "https://ddragon.leagueoflegends.com/cdn/img/"

        entity.slots.take(4).forEachIndexed { index, iconUrl ->
            if (index < keyRunesViews.size) {
                val imageView = keyRunesViews[index]
                Picasso.get().load(baseUrl + iconUrl).into(imageView)

                imageView.setOnClickListener {
                    RuneDetailDialogFragment.newInstance(
                        entity.runeNames[index],
                        entity.runeLongDescs[index],
                        baseUrl + iconUrl
                    ).show(parentFragmentManager, "rune_detail")
                }
            }
        }

// Lo mismo para las subrunas
        entity.slots.drop(3).take(9).chunked(3).forEachIndexed { rowIndex, chunk ->
            if (rowIndex < subRuneRows.size) {
                chunk.forEachIndexed { i, iconUrl ->
                    val indexGlobal = 3 + rowIndex * 3 + i
                    if (i < subRuneRows[rowIndex].size && indexGlobal < entity.slots.size) {
                        val imageView = subRuneRows[rowIndex][i]
                        Picasso.get().load(baseUrl + iconUrl).into(imageView)

                        imageView.setOnClickListener {
                            RuneDetailDialogFragment.newInstance(
                                entity.runeNames[indexGlobal],
                                entity.runeLongDescs[indexGlobal],
                                baseUrl + iconUrl
                            ).show(parentFragmentManager, "rune_detail")
                        }
                    }
                }
            }
        }

    }
}