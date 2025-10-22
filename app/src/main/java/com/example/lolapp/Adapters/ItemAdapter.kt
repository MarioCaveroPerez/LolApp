package com.example.lolapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.lolapp.Data.Champion
import com.example.lolapp.Data.Item
import com.example.lolapp.R

class ItemAdapter(private val activity: FragmentActivity, var itemList: List<Item>, val onItemClick: (String) -> Unit) : RecyclerView.Adapter<ItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_champion, parent, false)
        return ItemViewHolder(view)
    }
    fun updateList(newList: List<Item>) {
        itemList = newList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder,
        position: Int
    ) {
        holder.bind(itemList[position], activity, onItemClick)
    }

    override fun getItemCount() = itemList.size
}