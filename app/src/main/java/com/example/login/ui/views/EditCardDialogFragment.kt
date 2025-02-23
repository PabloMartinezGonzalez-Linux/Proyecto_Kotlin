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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditCardDialogFragment : DialogFragment() {

    private var _binding: DialogOptionsBinding? = null
    private val binding get() = _binding!!

    private var selectedImageUri: Uri? = null
    private var selectedBackgroundRes: Int = R.drawable.bg_gris
    private var cardItem: CardItem? = null

    var onSave: ((CardItem) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cardItem = arguments?.getParcelable(ARG_CARD_ITEM)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogOptionsBinding.inflate(inflater, container, false)

        if (cardItem == null) {
            cardItem = CardItem(imageRes = R.drawable.img_1, brand = "", model = "")
        }

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

            onSave?.invoke(newCard)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_CARD_ITEM = "arg_card_item"
        private const val REQUEST_IMAGE_PICKER = 1001

        fun newInstance(initialCard: CardItem?): EditCardDialogFragment {
            val fragment = EditCardDialogFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARG_CARD_ITEM, initialCard)
            fragment.arguments = bundle
            return fragment
        }
    }
}
