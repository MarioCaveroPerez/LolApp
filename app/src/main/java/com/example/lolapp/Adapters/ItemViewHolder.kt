package com.example.lolapp.Adapters

import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.lolapp.Activities.Items.ItemsDetail.ItemDetailBottomSheetFragment
import com.example.lolapp.Data.Item
import com.example.lolapp.databinding.ItemItemBinding
import com.squareup.picasso.Picasso

class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemItemBinding.bind(view)

    fun bind(item: Item, activity: FragmentActivity, onItemClick: (String) -> Unit) {
        binding.nameChampion.text = item.name
        Picasso.get().load("https://ddragon.leagueoflegends.com/cdn/15.19.1/img/item/${item.image.full}").into(binding.imageChampion)
        binding.root.setOnClickListener { onItemClick(item.name) }
        binding.root.setOnClickListener {
            val fragment = ItemDetailBottomSheetFragment(item)
            fragment.show(activity.supportFragmentManager, fragment.tag)
        }

    }
}