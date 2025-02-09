package com.example.login.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.login.R
import com.example.login.databinding.FragmentHomeBinding
import com.example.login.ui.adapter.CardAdapter
import com.example.login.ui.viewmodel.CardViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: CardAdapter
    private lateinit var viewModel: CardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(CardViewModel::class.java)

        adapter = CardAdapter(emptyList()) { position ->
            editCard(position)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.cards.observe(viewLifecycleOwner) { cards ->
            adapter.updateList(cards)
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewModel.cards.value?.get(position)?.let { card ->
                    viewModel.deleteCard(card)
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        binding.addCardButton.setOnClickListener {
            val dialogFragment = EditCardDialogFragment(null) { newCard ->
                viewModel.addCard(newCard)
            }
            dialogFragment.show(parentFragmentManager, "AddCardDialog")
        }

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
