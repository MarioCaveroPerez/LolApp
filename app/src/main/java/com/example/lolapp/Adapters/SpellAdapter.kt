package com.example.lolapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lolapp.Data.SpellItem
import com.example.lolapp.R
import com.squareup.picasso.Picasso

class SpellsAdapter(private val spellsList: List<SpellItem>) :
    RecyclerView.Adapter<SpellsAdapter.SpellViewHolder>() {

    inner class SpellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivSpellImage: ImageView = itemView.findViewById(R.id.ivSpellImage)
        val tvSpellName: TextView = itemView.findViewById(R.id.tvSpellName)
        val tvSpellCost: TextView = itemView.findViewById(R.id.tvSpellCost)
        val tvSpellDescription: TextView = itemView.findViewById(R.id.tvSpellDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpellViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_spells, parent, false)
        return SpellViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpellViewHolder, position: Int) {
        val spell = spellsList[position]

        holder.tvSpellName.text = spell.name
        holder.tvSpellCost.text = "Coste: ${spell.cost}"
        holder.tvSpellDescription.text = spell.description

        val imageUrl = if (spell.id == "passive") {
            "https://ddragon.leagueoflegends.com/cdn/15.19.1/img/passive/${spell.image.full}"
        } else {
            "https://ddragon.leagueoflegends.com/cdn/15.19.1/img/spell/${spell.image.full}"
        }
        Picasso.get().load(imageUrl).into(holder.ivSpellImage)
    }

    override fun getItemCount() = spellsList.size
}
