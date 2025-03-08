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
import com.example.login.domain.models.CardRequest
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

        // ✅ Inicializar el adapter con una lista vacía
        adapter = CardAdapter(mutableListOf()) { position ->
            // Aquí puedes manejar la acción al hacer clic en una card
        }

        // Configurar RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // ✅ Agregar la funcionalidad de deslizar para eliminar
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val cardToDelete = adapter.getItemAt(position)

                cardViewModel.deleteCard(cardToDelete)
                adapter.removeItem(position)
            }

        })

        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        // ✅ Observar las cards del ViewModel y actualizar la UI
        viewLifecycleOwner.lifecycleScope.launch {
            cardViewModel.cards.collectLatest { cards ->
                Log.d("HomeFragment", "📌 Nueva lista de tarjetas recibida: ${cards.size} tarjetas")
                adapter.updateList(cards) // 🔥 Se actualiza la lista visualmente
            }
        }

        // ✅ Observar posibles errores
        viewLifecycleOwner.lifecycleScope.launch {
            cardViewModel.errorMessage.collectLatest { error ->
                error?.let {
                    Log.e("HomeFragment", "❌ Error: $it")
                }
            }
        }

        // ✅ Llamar a fetchCards() para obtener las cards desde la API
        cardViewModel.fetchCards()

        // ✅ Configurar el botón para añadir una tarjeta nueva
        binding.addCardButton.setOnClickListener {
            val newCard = CardRequest(
                photo = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUA...", // 🔹 Mantener la estructura correcta
                name = "Card de Ejemplo",
                description = "Prueba",
                averageRating = 4.5,
                hasImprovements = true
            )

            cardViewModel.addCard(newCard)

            // 🔄 Después de añadir, actualizar la lista
            cardViewModel.fetchCards()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
