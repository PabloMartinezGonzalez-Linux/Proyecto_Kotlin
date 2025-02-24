package com.example.login.ui.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.login.databinding.ItemCardBinding
import com.example.login.domain.models.CardItem

class CardAdapter(
    private var items: List<CardItem>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    inner class CardViewHolder(private val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CardItem) {
            binding.Marca.text = item.brand
            binding.Modelo.text = item.model

            if (!item.imageBase64.isNullOrEmpty()) {
                try {
                    val decodedBytes = Base64.decode(item.imageBase64, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                    binding.productImage.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                    item.imageRes?.let { binding.productImage.setImageResource(it) }
                }
            } else {
                item.imageRes?.let { binding.productImage.setImageResource(it) }
            }

            binding.root.setOnClickListener { onClick(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newItems: List<CardItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
