package com.example.login.ui.views

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.login.R
import com.example.login.domain.models.CardItem
import com.example.login.domain.models.CardRequest
import com.example.login.ui.viewmodel.CardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardDialogFragment(private val card: CardItem? = null) : DialogFragment() {

    private val cardViewModel: CardViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.dialog_card, null)

        // Obtener referencias a los campos del diálogo
        val etName = view.findViewById<EditText>(R.id.etName)
        val etDescription = view.findViewById<EditText>(R.id.etDescription)
        val etAverageRating = view.findViewById<EditText>(R.id.etAverageRating)
        val cbHasImprovements = view.findViewById<CheckBox>(R.id.cbHasImprovements)
        val btnSave = view.findViewById<ImageButton>(R.id.btnSave)

        // Si estamos editando, rellenar los campos con los datos actuales
        card?.let {
            etName.setText(it.name)
            etDescription.setText(it.description)
            etAverageRating.setText(it.averageRating.toString())
            cbHasImprovements.isChecked = it.hasImprovements
        }

        // Acción al presionar el botón Guardar
        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val averageRating = etAverageRating.text.toString().toDoubleOrNull() ?: 0.0
            val hasImprovements = cbHasImprovements.isChecked

            // Crear un objeto CardRequest con los valores ingresados
            val updatedCard = CardRequest(
                photo = card?.photo ?: "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUA...", // Imagen de prueba
                name = name,
                description = description,
                averageRating = averageRating,
                hasImprovements = hasImprovements
            )

            if (card == null) {
                // Si es una nueva tarjeta, la añadimos
                cardViewModel.addCard(updatedCard)
            } else {
                // Si es una edición, actualizamos la tarjeta existente
                card.id?.let { id ->
                    cardViewModel.updateCard(id, updatedCard)
                }
            }

            dismiss() // Cierra el diálogo después de guardar
        }

        builder.setView(view)
        return builder.create()
    }
}

