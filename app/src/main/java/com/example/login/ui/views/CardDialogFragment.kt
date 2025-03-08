package com.example.login.ui.views

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.login.R
import com.example.login.domain.models.CardItem
import com.example.login.domain.models.CardRequest
import com.example.login.ui.viewmodel.CardViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class CardDialogFragment(private val card: CardItem? = null) : DialogFragment() {

    private val cardViewModel: CardViewModel by activityViewModels()
    private var selectedImageBase64: String? = null // Para almacenar la imagen en Base64

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.dialog_card, null)

        // Obtener referencias a los elementos del diálogo
        val etName = view.findViewById<EditText>(R.id.etName)
        val etDescription = view.findViewById<EditText>(R.id.etDescription)
        val etAverageRating = view.findViewById<EditText>(R.id.etAverageRating)
        val cbHasImprovements = view.findViewById<CheckBox>(R.id.cbHasImprovements)
        val ivImage = view.findViewById<ImageView>(R.id.ivImage)
        val btnSelectFromGallery = view.findViewById<ImageButton>(R.id.btnSelectFromGallery)
        val btnSave = view.findViewById<ImageButton>(R.id.btnSave)

        // Si estamos editando, rellenar los campos con los datos actuales
        card?.let {
            etName.setText(it.name)
            etDescription.setText(it.description)
            etAverageRating.setText(it.averageRating.toString())
            cbHasImprovements.isChecked = it.hasImprovements

            // Si la tarjeta ya tiene una imagen, la mostramos
            if (!it.photo.isNullOrEmpty()) {
                val bitmap = decodeBase64(it.photo)
                if (bitmap != null) ivImage.setImageBitmap(bitmap)
            }
        }

        // 🔹 Abrir la galería para seleccionar una imagen
        val pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val imageUri = result.data?.data
                    imageUri?.let {
                        ivImage.setImageURI(it) // Mostrar la imagen seleccionada
                        selectedImageBase64 = encodeImageToBase64(it) // Convertir a Base64
                    }
                }
            }

        btnSelectFromGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImageLauncher.launch(intent)
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val averageRating = etAverageRating.text.toString().toDoubleOrNull() ?: 0.0
            val hasImprovements = cbHasImprovements.isChecked

            // Si la imagen no cambia, usar la existente o Base64 de la galería
            val photoBase64 = selectedImageBase64 ?: card?.photo ?: "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUA..."

            val newCard = CardRequest(
                photo = photoBase64,
                name = name,
                description = description,
                averageRating = averageRating,
                hasImprovements = hasImprovements
            )

            if (card == null) {
                cardViewModel.addCard(newCard)
            } else {
                card.id?.let { id ->
                    cardViewModel.updateCard(id, newCard)
                }
            }

            dismiss() // Cierra el diálogo después de guardar
        }

        builder.setView(view)
        return builder.create()
    }

    private fun encodeImageToBase64(imageUri: Uri): String? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream) // Comprime al 50%
            val byteArray = outputStream.toByteArray()

            "data:image/jpeg;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    private fun decodeBase64(base64Str: String): Bitmap? {
        return try {
            val base64Data = base64Str.substringAfter("base64,") // Elimina el prefijo "data:image/png;base64,"
            val decodedBytes = Base64.decode(base64Data, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
