package com.example.login.ui.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.login.databinding.FragmentHomeBinding
import com.example.login.ui.adapter.CardAdapter
import com.example.login.ui.viewmodel.CardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val cardViewModel: CardViewModel by viewModels()
    private lateinit var adapter: CardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // 🔹 Ahora pasamos una función de clic en las cards
        adapter = CardAdapter(mutableListOf()) { selectedCard ->
            CardDialogFragment(selectedCard).show(parentFragmentManager, "CardDialogFragment")
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val cardToDelete = adapter.getItemAt(position)
                cardViewModel.deleteCard(cardToDelete)
                adapter.removeItem(position)
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        viewLifecycleOwner.lifecycleScope.launch {
            cardViewModel.cards.collectLatest { cards ->
                Log.d("HomeFragment", "📌 Nueva lista de tarjetas recibida: ${cards.size} tarjetas")
                adapter.updateList(cards)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            cardViewModel.errorMessage.collectLatest { error ->
                error?.let { Log.e("HomeFragment", "❌ Error: $it") }
            }
        }

        cardViewModel.fetchCards()

        // 🔹 Si se pulsa el botón "Añadir", abrir el diálogo en modo CREACIÓN
        binding.addCardButton.setOnClickListener {
            CardDialogFragment().show(parentFragmentManager, "CardDialogFragment")
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
