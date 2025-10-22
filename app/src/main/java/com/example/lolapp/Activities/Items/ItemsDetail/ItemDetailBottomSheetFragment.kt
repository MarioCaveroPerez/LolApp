package com.example.lolapp.Activities.Items.ItemsDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.example.lolapp.Data.Item
import com.example.lolapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso

class ItemDetailBottomSheetFragment(private val item: Item) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.bottom_sheet_item_detail, container, false)

        val ivItemImage = view.findViewById<ImageView>(R.id.ivItemImage)
        val tvItemName = view.findViewById<TextView>(R.id.tvItemName)
        val tvItemPrice = view.findViewById<TextView>(R.id.tvItemPrice)
        val tvItemDescription = view.findViewById<TextView>(R.id.tvItemDescription)
        val buildTreeContainer = view.findViewById<LinearLayout>(R.id.buildTreeContainer)
        val buildFromContainer = view.findViewById<LinearLayout>(R.id.fromContainer)

        // Setear datos básicos
        tvItemName.text = item.name
        tvItemPrice.text = "${item.gold.total} gold"
        val rawDescription = item.description
        val cleanDescription = cleanItemDescriptionPreserveAbilities(rawDescription)
        tvItemDescription.text = cleanDescription
        Picasso.get()
            .load("https://ddragon.leagueoflegends.com/cdn/15.19.1/img/item/${item.image.full}")
            .into(ivItemImage)

        // Árbol de build
        if (!item.into.isNullOrEmpty()) {
            buildTreeContainer.visibility = View.VISIBLE
            buildTreeContainer.removeAllViews()
            buildFromContainer.visibility = View.VISIBLE
            buildFromContainer.removeAllViews()

            item.into.forEach { itemId ->
                val imageView = ImageView(requireContext())
                val size = 64.dp(requireContext() as FragmentActivity)
                val layoutParams = LinearLayout.LayoutParams(size, size)
                layoutParams.setMargins(8, 0, 8, 0)
                imageView.layoutParams = layoutParams

                Picasso.get()
                    .load("https://ddragon.leagueoflegends.com/cdn/15.19.1/img/item/$itemId.png")
                    .into(imageView)

                buildTreeContainer.addView(imageView)
            }
        } else {
            buildTreeContainer.visibility = View.GONE
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheet = view.parent as? View
        val behavior = com.google.android.material.bottomsheet.BottomSheetBehavior.from(bottomSheet!!)

        val expandedSection = view.findViewById<LinearLayout>(R.id.expandedSection)
        val buildTreeContainer = view.findViewById<LinearLayout>(R.id.buildTreeContainer)
        val fromContainer = view.findViewById<LinearLayout>(R.id.fromContainer)

        // Mostrar/ocultar según estado
        behavior.addBottomSheetCallback(object : com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                expandedSection.visibility = if (newState == com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        // Llenar "Crafteable" (into)
        item.into?.distinctBy { item.description }?.forEach { id ->
            val iv = ImageView(requireContext())
            val size = 64.dp(requireContext() as FragmentActivity)
            iv.layoutParams = LinearLayout.LayoutParams(size, size).apply { setMargins(8,0,8,0) }
            Picasso.get().load("https://ddragon.leagueoflegends.com/cdn/15.19.1/img/item/$id.png").into(iv)
            buildTreeContainer.addView(iv)
        }

        item.from?.distinctBy { item.name }?.forEach { id ->
            val iv = ImageView(requireContext())
            val size = 64.dp(requireContext() as FragmentActivity)
            iv.layoutParams = LinearLayout.LayoutParams(size, size).apply { setMargins(8,0,8,0) }
            Picasso.get().load("https://ddragon.leagueoflegends.com/cdn/15.19.1/img/item/$id.png").into(iv)
            fromContainer.addView(iv)
        }
    }

    fun cleanItemDescriptionPreserveAbilities(raw: String): String {
        // Decodificar los caracteres Unicode
        val decoded = raw.replace("\\u003C".toRegex(), "<")
            .replace("\\u003E".toRegex(), ">")

        // Reemplazar saltos de línea <br> por \n
        var withLineBreaks = decoded.replace("<br>".toRegex(), "\n")

        // Reemplazar etiquetas <passive> y <active> por algún marcador visible
        // Por ejemplo, ponemos **PASSIVE:** o **ACTIVE:** antes del texto
        withLineBreaks = withLineBreaks.replace("<passive>(.*?)</passive>".toRegex()) { match ->
            "PASIVA: ${match.groupValues[1]}"
        }

        withLineBreaks = withLineBreaks.replace("<active>(.*?)</active>".toRegex()) { match ->
            "ACTIVA: ${match.groupValues[1]}"
        }

        // Eliminar todas las demás etiquetas
        val clean = withLineBreaks.replace("<.*?>".toRegex(), "")

        return clean
    }


    // Extensión para dp → px
    private fun Int.dp(context: FragmentActivity): Int =
        (this * context.resources.displayMetrics.density).toInt()
}