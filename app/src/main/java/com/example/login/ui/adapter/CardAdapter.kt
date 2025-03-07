package com.example.login.ui.adapter

import android.graphics.Bitmap
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
                val bitmap = decodeBase64(item.imageBase64)
                if (bitmap != null) {
                    binding.productImage.setImageBitmap(bitmap)
                } else {
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

    fun updateList(newItems: List<CardItem>?) {
        items = newItems ?: emptyList() // Evita valores nulos
        notifyDataSetChanged()
    }

    private fun decodeBase64(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
