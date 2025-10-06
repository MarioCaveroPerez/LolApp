package com.example.lolapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lolapp.Data.Champion
import com.example.lolapp.R

class ChampionAdapter(var championList: List<Champion>, val onItemClick: (Champion) -> Unit) : RecyclerView.Adapter<ChampionViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChampionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_main, parent, false)
        return ChampionViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ChampionViewHolder,
        position: Int
    ) {
        val item = championList[position]
    }

    override fun getItemCount() = championList.size
}