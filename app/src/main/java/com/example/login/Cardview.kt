package com.example.login

import CardAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.login.databinding.ActivityRecyclerviewBinding

class Cardview : AppCompatActivity() {

    private lateinit var binding: ActivityRecyclerviewBinding
    private val cardItems = mutableListOf<CardItem>()
    private lateinit var adapter: CardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = resources.getColor(R.color.negro100)

        // Agregar tarjetas iniciales
        cardItems.add(CardItem(R.drawable.img_1, "KAWASAKI", "777"))
        cardItems.add(CardItem(R.drawable.img_1, "YAMAHA", "555"))
        // ...

        // Configurar el adaptador
        adapter = CardAdapter(cardItems) { position ->
            editCard(position)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Configurar el helper para deslizamiento
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                cardItems.removeAt(position)
                adapter.notifyItemRemoved(position)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        // Configurar el botón para añadir nueva tarjeta
        binding.addCardButton.setOnClickListener {
            val dialogFragment = EditCardDialogFragment(null, cardItems) { newCard ->
                // Añadir la tarjeta a la lista
                cardItems.add(newCard)
                adapter.notifyItemInserted(cardItems.size - 1)
            }
            dialogFragment.show(supportFragmentManager, "AddCardDialog")
        }
    }

    private fun editCard(position: Int) {
        val dialogFragment = EditCardDialogFragment(position, cardItems) { updatedCard ->
            cardItems[position] = updatedCard
            adapter.notifyItemChanged(position)
        }
        dialogFragment.show(supportFragmentManager, "EditCardDialog")
    }
}

