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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso

class ItemDetailBottomSheetFragment(
    private val item: Item,
    private val allItems: List<Item>
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.bottom_sheet_item_detail, container, false)

        val ivItemImage = view.findViewById<ImageView>(R.id.ivItemImage)
        val tvItemName = view.findViewById<TextView>(R.id.tvItemName)
        val tvItemPrice = view.findViewById<TextView>(R.id.tvItemPrice)
        val tvItemDescription = view.findViewById<TextView>(R.id.tvItemDescription)

        // Datos básicos
        tvItemName.text = item.name
        tvItemPrice.text = "${item.gold.total} gold"
        tvItemDescription.text = cleanItemDescriptionPreserveAbilities(item.description)
        Picasso.get().load("https://ddragon.leagueoflegends.com/cdn/15.21.1/img/item/${item.image.full}")
            .into(ivItemImage)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            ?: return
        val behavior = BottomSheetBehavior.from(bottomSheet)

        // Inicialización
        behavior.isFitToContents = true
        behavior.isDraggable = true
        behavior.skipCollapsed = false
        view.post {
            val tvItemDescription = view.findViewById<TextView>(R.id.tvItemDescription)
            val descriptionHeight = tvItemDescription.height
            val padding = 75.dp(requireContext() as FragmentActivity) // opcional, para espacio visual
            behavior.peekHeight = descriptionHeight + padding
        }
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED

        // Referencias de sección expandida
        val expandedSection = view.findViewById<LinearLayout>(R.id.expandedSection)
        val buildTreeContainer = view.findViewById<LinearLayout>(R.id.buildTreeContainer)
        val fromContainer = view.findViewById<LinearLayout>(R.id.fromContainer)
        expandedSection.alpha = 0f
        expandedSection.visibility = View.VISIBLE

        // Animación inicial de apertura
        bottomSheet.translationY = bottomSheet.height.toFloat()
        bottomSheet.animate()
            .translationY(0f)
            .setDuration(300)
            .setInterpolator(android.view.animation.AccelerateDecelerateInterpolator())
            .start()

        // Fade in/out de la sección expandida al arrastrar
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                expandedSection.alpha = slideOffset.coerceIn(0f, 1f)
            }
        })

        // --- Fill INTO ---
        item.into
            ?.mapNotNull { id -> allItems.find { it.image.full.contains(id) && it.maps["11"] == true && it.gold.purchasable == true } }
            ?.distinctBy { it.name }
            ?.sortedBy { it.gold.total }
            ?.forEach { filteredItem ->
                val iv = ImageView(requireContext())
                val size = 64.dp(requireContext() as FragmentActivity)
                iv.layoutParams = LinearLayout.LayoutParams(size, size).apply { setMargins(8, 0, 8, 0) }
                Picasso.get().load("https://ddragon.leagueoflegends.com/cdn/15.21.1/img/item/${filteredItem.image.full}")
                    .into(iv)
                iv.setOnClickListener {
                    dismiss()
                    val fragment = ItemDetailBottomSheetFragment(filteredItem, allItems)
                    fragment.show(parentFragmentManager, fragment.tag)
                }
                buildTreeContainer.addView(iv)
            }


        // --- Fill FROM ---
        item.from
            ?.mapNotNull { id -> allItems.find { it.image.full.contains(id) && it.maps["11"] == true && it.gold.purchasable == true } }
            ?.sortedBy { it.gold.total }
            ?.forEach { filteredItem ->
                val iv = ImageView(requireContext())
                val size = 64.dp(requireContext() as FragmentActivity)
                iv.layoutParams = LinearLayout.LayoutParams(size, size).apply { setMargins(8, 0, 8, 0) }
                Picasso.get().load("https://ddragon.leagueoflegends.com/cdn/15.21.1/img/item/${filteredItem.image.full}")
                    .into(iv)
                iv.setOnClickListener {
                    dismiss()
                    val fragment = ItemDetailBottomSheetFragment(filteredItem, allItems)
                    fragment.show(parentFragmentManager, fragment.tag)
                }
                fromContainer.addView(iv)
            }
    }

    private fun cleanItemDescriptionPreserveAbilities(raw: String): String {
        val decoded = raw.replace("\\u003C".toRegex(), "<").replace("\\u003E".toRegex(), ">")
        var text = decoded.replace("<br>".toRegex(), "\n")
        text = text.replace("<passive>(.*?)</passive>".toRegex()) { "PASIVA: ${it.groupValues[1]}" }
        text = text.replace("<active>(.*?)</active>".toRegex()) { "ACTIVA: ${it.groupValues[1]}" }
        return text.replace("<.*?>".toRegex(), "")
    }

    private fun Int.dp(context: FragmentActivity) = (this * context.resources.displayMetrics.density).toInt()
}
