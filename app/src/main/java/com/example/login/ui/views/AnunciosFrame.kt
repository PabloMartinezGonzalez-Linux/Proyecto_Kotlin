package com.example.login.ui.views

import CardAnuncioAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.login.R
import com.example.login.databinding.FragmentAnunciosBinding
import com.example.login.domain.models.CardAnuncio
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnunciosFragment : Fragment() {

    private var _binding: FragmentAnunciosBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CardAnuncioAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnunciosBinding.inflate(inflater, container, false)

        val anuncioCards = listOf(
            CardAnuncio( R.drawable.disney, 3),
            CardAnuncio(  R.drawable.redbull, 1),
            CardAnuncio(R.drawable.fanta, 2),
            CardAnuncio(R.drawable.nestea, 0),
            CardAnuncio(R.drawable.prime, 3)
        )

        adapter = CardAnuncioAdapter(anuncioCards)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
