package com.example.lolapp.Adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.lolapp.Data.Champion
import com.example.lolapp.databinding.ItemChampionBinding
import com.squareup.picasso.Picasso

class ChampionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemChampionBinding.bind(view)

    fun bind(champion: Champion, onItemSelected: (String) -> Unit){
        binding.nameChampion.text = champion.name
        Picasso.get().load("https://ddragon.leagueoflegends.com/cdn/15.19.1/img/champion/${champion.image.full}").into(binding.imageChampion)
        binding.root.setOnClickListener { onItemSelected(champion.id) }
    }
}