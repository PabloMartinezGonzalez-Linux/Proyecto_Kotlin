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
    private var items: MutableList<CardItem>,
    private val onClick: (CardItem) -> Unit
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    inner class CardViewHolder(private val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CardItem) {
            binding.cardName.text = item.name
            binding.cardDescription.text = item.description
            binding.cardRating.text = "⭐ ${item.averageRating}"

            if (!item.photo.isNullOrEmpty()) {
                val bitmap = decodeBase64(item.photo)
                if (bitmap != null) {
                    binding.cardImage.setImageBitmap(bitmap)
                } else {
                    binding.cardImage.setImageResource(android.R.drawable.ic_menu_gallery)
                }
            } else {
                binding.cardImage.setImageResource(android.R.drawable.ic_menu_gallery)
            }

            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun getItemAt(position: Int): CardItem {
        return items[position]
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newItems: List<CardItem>?) {
        items.clear()
        if (newItems != null) {
            items.addAll(newItems)
        }
        notifyDataSetChanged()
    }

    private fun decodeBase64(base64Str: String): Bitmap? {
        return try {
            val base64Data = base64Str.substringAfter("base64,")
            val decodedBytes = Base64.decode(base64Data, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
