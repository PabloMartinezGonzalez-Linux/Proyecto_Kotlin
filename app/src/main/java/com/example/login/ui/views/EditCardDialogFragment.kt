package com.example.login.ui.views

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.login.R
import com.example.login.databinding.DialogOptionsBinding
import com.example.login.domain.models.CardItem

class EditCardDialogFragment(
    private val initialCard: CardItem?,
    private val onSave: (CardItem) -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogOptionsBinding
    private var selectedImageUri: Uri? = null
    private var selectedBackgroundRes: Int = R.drawable.bg_gris
    private var cardItem: CardItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogOptionsBinding.inflate(inflater, container, false)

        cardItem = initialCard ?: CardItem(imageRes = R.drawable.img_1, brand = "", model = "")

        cardItem?.let {
            binding.etMarca.setText(it.brand)
            binding.etModelo.setText(it.model)
            it.imageUri?.let { uri ->
                binding.ivImage.setImageURI(uri)
            } ?: it.imageRes?.let { res ->
                binding.ivImage.setImageResource(res)
            }
            selectedBackgroundRes = it.bgImageRes
            when (selectedBackgroundRes) {
                R.drawable.bg_rojo -> binding.backgroundSelector.check(R.id.rbRed)
                R.drawable.bg_azul -> binding.backgroundSelector.check(R.id.rbBlue)
                R.drawable.bg_tex_verde -> binding.backgroundSelector.check(R.id.rbGreen)
                else -> binding.backgroundSelector.check(R.id.rbGris)
            }
        }

        binding.backgroundSelector.setOnCheckedChangeListener { _, checkedId ->
            selectedBackgroundRes = when (checkedId) {
                R.id.rbRed -> R.drawable.bg_rojo
                R.id.rbBlue -> R.drawable.bg_azul
                R.id.rbGreen -> R.drawable.bg_tex_verde
                else -> R.drawable.bg_gris
            }
        }

        binding.btnChangeImage.setOnClickListener {
            openImagePicker()
        }

        binding.btnSave.setOnClickListener {
            val brand = binding.etMarca.text.toString()
            val model = binding.etModelo.text.toString()

            val newCard = cardItem?.apply {
                this.imageUri = selectedImageUri
                this.brand = brand
                this.model = model
                this.bgImageRes = selectedBackgroundRes
            } ?: CardItem(
                imageRes = R.drawable.img_1,
                brand = brand,
                model = model,
                imageUri = selectedImageUri,
                bgImageRes = selectedBackgroundRes
            )

            onSave(newCard)
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

    companion object {
        private const val REQUEST_IMAGE_PICKER = 1001
    }
}
