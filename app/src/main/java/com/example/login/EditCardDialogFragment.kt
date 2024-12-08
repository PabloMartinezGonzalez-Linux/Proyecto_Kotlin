package com.example.login

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.login.databinding.DialogOptionsBinding

class EditCardDialogFragment(
    private val position: Int,
    private val cardItems: MutableList<CardItem>, // Lista de tarjetas
    private val onSave: (CardItem) -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogOptionsBinding
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogOptionsBinding.inflate(inflater, container, false)

        val cardItem = cardItems[position]

        // Prellenar los campos con los valores actuales
        binding.etMarca.setText(cardItem.brand)
        binding.etModelo.setText(cardItem.model)
        binding.ivImage.setImageResource(cardItem.imageRes)

        // Cambiar imagen
        binding.btnChangeImage.setOnClickListener {
            openImagePicker()
        }

        // Guardar cambios
        binding.btnSave.setOnClickListener {
            val updatedCard = CardItem(
                imageRes = selectedImageUri?.let { uri -> getImageResourceFromUri(uri) } ?: cardItem.imageRes,
                brand = binding.etMarca.text.toString(),
                model = binding.etModelo.text.toString()
            )
            onSave(updatedCard)
            dismiss()
        }

        return binding.root
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICKER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_PICKER) {
            data?.data?.let { uri ->
                selectedImageUri = uri
                binding.ivImage.setImageURI(uri)
            }
        }
    }

    private fun getImageResourceFromUri(uri: Uri): Int {
        return R.drawable.img_1
    }

    companion object {
        private const val REQUEST_IMAGE_PICKER = 1001
    }
}

