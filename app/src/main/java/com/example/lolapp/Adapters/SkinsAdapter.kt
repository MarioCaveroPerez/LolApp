package com.example.lolapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lolapp.Data.Skins
import com.example.lolapp.R
import com.example.lolapp.databinding.ItemSkinBinding
import com.squareup.picasso.Picasso

class SkinsAdapter(private var skinsList: List<Skins>) :
    RecyclerView.Adapter<SkinsAdapter.SkinsViewHolder>() {

    inner class SkinsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivSkin = itemView.findViewById<ImageView>(R.id.ivSkin)
        val tvSkinName = itemView.findViewById<TextView>(R.id.tvSkinName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkinsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_skin, parent, false)
        return SkinsViewHolder(view)
    }

    override fun getItemCount(): Int = skinsList.size

    override fun onBindViewHolder(holder: SkinsViewHolder, position: Int) {
        val skin = skinsList[position]
        holder.tvSkinName.text = skin.name

        val splashUrl = "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/${skin.num}.jpg"
        // Asegúrate de ajustar la URL según tu lógica de skins
        Picasso.get()
            .load(splashUrl)
            .fit()
            .centerCrop()
            .into(holder.ivSkin)
    }

    fun updateList(newList: List<Skins>) {
        skinsList = newList
        notifyDataSetChanged()
    }
}