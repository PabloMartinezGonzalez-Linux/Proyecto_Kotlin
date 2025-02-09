package com.example.login.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.login.R
import com.example.login.databinding.FragmentAnunciosBinding
import com.example.login.domain.models.CardItem
import com.example.login.ui.adapter.CardAdapter

class AnunciosFragment : Fragment() {

    private lateinit var binding: FragmentAnunciosBinding
    private val cardItems = mutableListOf<CardItem>()
    private lateinit var adapter: CardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnunciosBinding.inflate(inflater, container, false)

        // Configurar RecyclerView
        cardItems.add(CardItem(R.drawable.img_1, "KAWASAKI", "777"))
        cardItems.add(CardItem(R.drawable.img_1, "YAMAHA", "555"))

        adapter = CardAdapter(cardItems) { position ->
            editCard(position)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // Configurar swipe para eliminar
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                cardItems.removeAt(position)
                adapter.notifyItemRemoved(position)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        return binding.root
    }

    private fun editCard(position: Int) {
        val dialogFragment = EditCardDialogFragment(position, cardItems) { updatedCard ->
            cardItems[position] = updatedCard
            adapter.notifyItemChanged(position)
        }
        dialogFragment.show(parentFragmentManager, "EditCardDialog")
    }
}
