package com.example.login.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.login.data.models.CardRequest
import com.example.login.databinding.FragmentHomeBinding
import com.example.login.ui.adapter.CardAdapter
import com.example.login.ui.viewmodel.CardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CardAdapter

    private val viewModel: CardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        adapter = CardAdapter(emptyList()) { position ->
            editCard(position)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // Observamos las tarjetas desde el backend
        viewModel.cards.observe(viewLifecycleOwner) { cards ->
            adapter.updateList(cards)
        }

        // Swipe para eliminar una tarjeta
        val itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val card = viewModel.cards.value?.getOrNull(position)
                if (card?.id != null) {
                    Log.d("HomeFragment", "Eliminando tarjeta con ID: ${card.id}") // 🔥 Depuración
                    viewModel.deleteCard(card.id)
                } else {
                    Log.e("HomeFragment", "Error: ID de tarjeta es nulo")
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        // Botón para agregar una tarjeta
        binding.addCardButton.setOnClickListener {
            val dialogFragment = EditCardDialogFragment.newInstance(null)
            dialogFragment.onSave = { newCard ->
                val request = CardRequest(
                    imageBase64 = viewModel.imageBase64.value ?: "",
                    brand = newCard.brand,
                    model = newCard.model,
                    averageRating = 0.0,
                    hasImprovements = false
                )
                Log.d("HomeFragment", "Añadiendo nueva tarjeta: $request") // 🔥 Depuración
                viewModel.addCard(request)
            }
            dialogFragment.show(parentFragmentManager, "AddCardDialog")
        }

        return binding.root
    }

    private fun editCard(position: Int) {
        val cardToEdit = viewModel.cards.value?.getOrNull(position)
        if (cardToEdit != null) {
            Log.d("HomeFragment", "Editando tarjeta con ID: ${cardToEdit.id}") // 🔥 Depuración

            val dialogFragment = EditCardDialogFragment.newInstance(cardToEdit)
            dialogFragment.onSave = { updatedCard ->
                val request = CardRequest(
                    id = updatedCard.id,   // 🔥 Ahora se pasa el `id`
                    imageBase64 = viewModel.imageBase64.value ?: "",
                    brand = updatedCard.brand,
                    model = updatedCard.model,
                    averageRating = 0.0,
                    hasImprovements = false
                )

                if (updatedCard.id != null) {
                    Log.d("HomeFragment", "Actualizando tarjeta con ID: ${updatedCard.id}") // 🔥 Depuración
                    viewModel.updateCard(updatedCard.id, request)
                } else {
                    Log.e("HomeFragment", "Error: ID de tarjeta es nulo")
                }
            }
            dialogFragment.show(parentFragmentManager, "EditCardDialog")
        } else {
            Log.e("HomeFragment", "Error: No se encontró la tarjeta para editar")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
