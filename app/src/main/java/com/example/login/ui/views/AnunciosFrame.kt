package com.example.login.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.login.databinding.FragmentAnunciosBinding
import com.example.login.ui.adapter.CardAdapter
import com.example.login.ui.viewmodel.CardViewModel

class AnunciosFragment : Fragment() {

    private lateinit var binding: FragmentAnunciosBinding
    private lateinit var adapter: CardAdapter
    private lateinit var viewModel: CardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnunciosBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(CardViewModel::class.java)

        adapter = CardAdapter(emptyList()) { position ->
            editCard(position)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.cards.observe(viewLifecycleOwner) { cards ->
            adapter.updateList(cards)
        }

        val itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: androidx.recyclerview.widget.RecyclerView,
                viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                target: androidx.recyclerview.widget.RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(
                viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                direction: Int
            ) {
                val position = viewHolder.adapterPosition
                viewModel.cards.value?.get(position)?.let { card ->
                    viewModel.deleteCard(card)
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        return binding.root
    }

    private fun editCard(position: Int) {
        val cardToEdit = viewModel.cards.value?.get(position)
        if (cardToEdit != null) {
            val dialogFragment = EditCardDialogFragment(cardToEdit) { updatedCard ->
                viewModel.deleteCard(cardToEdit)
                viewModel.addCard(updatedCard)
            }
            dialogFragment.show(parentFragmentManager, "EditCardDialog")
        }
    }

}
