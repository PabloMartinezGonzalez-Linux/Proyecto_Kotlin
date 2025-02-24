package com.example.login.ui.views

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.login.R
import com.example.login.databinding.DialogOptionsBinding
import com.example.login.domain.models.CardItem
import com.example.login.ui.viewmodel.CardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditCardDialogFragment : DialogFragment() {

    private var _binding: DialogOptionsBinding? = null
    private val binding get() = _binding!!

    private var capturedBitmap: Bitmap? = null
    private var selectedBackgroundRes: Int = R.drawable.bg_gris
    private var cardItem: CardItem? = null

    var onSave: ((CardItem) -> Unit)? = null

    private lateinit var cameraPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var takePictureLauncher: ActivityResultLauncher<Void?>
    private lateinit var galleryLauncher: ActivityResultLauncher<String>

    private val viewModel: CardViewModel by activityViewModels()

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cardItem = arguments?.getParcelable(ARG_CARD_ITEM)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogOptionsBinding.inflate(inflater, container, false)
        setupLaunchers()

        if (cardItem == null) {
            cardItem = CardItem(
                imageRes = R.drawable.img_1,
                brand = "",
                model = "",
                bgImageRes = 0,
                imageBase64 = null
            )
        }

        cardItem?.let {
            binding.etMarca.setText(it.brand)
            binding.etModelo.setText(it.model)
            it.imageBase64?.let { base64 ->
                val bitmap = base64ToBitmap(base64)
                if (bitmap != null) {
                    binding.ivImage.setImageBitmap(bitmap)
                }
            } ?: it.imageRes?.let { res ->
                binding.ivImage.setImageResource(res)
            }
        }


        binding.btnChangeImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.CAMERA
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                takePictureLauncher.launch(null)
            } else {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }

        binding.btnSelectFromGallery.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.btnSave.setOnClickListener {
            val brand = binding.etMarca.text.toString()
            val model = binding.etModelo.text.toString()
            Log.d("EditCardDialog", "Valor de imageBase64 en ViewModel: ${viewModel.imageBase64.value}")

            val newCard = cardItem?.apply {
                this.brand = brand
                this.model = model
                this.bgImageRes = selectedBackgroundRes
                this.imageBase64 = viewModel.imageBase64.value
            } ?: CardItem(
                imageRes = R.drawable.img_1,
                brand = brand,
                model = model,
                bgImageRes = selectedBackgroundRes,
                imageBase64 = viewModel.imageBase64.value
            )

            onSave?.invoke(newCard)
            dismiss()
        }

        return binding.root
    }

    private fun setupLaunchers() {
        cameraPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                takePictureLauncher.launch(null)
            } else {
                Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }

        takePictureLauncher = registerForActivityResult(
            ActivityResultContracts.TakePicturePreview()
        ) { bitmap ->
            if (bitmap != null) {
                capturedBitmap = bitmap
                binding.ivImage.setImageBitmap(bitmap)
                viewModel.processCapturedImage(bitmap)
            }
        }

        galleryLauncher = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
                    capturedBitmap = bitmap
                    binding.ivImage.setImageBitmap(bitmap)
                    viewModel.processCapturedImage(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun base64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_CARD_ITEM = "arg_card_item"
        fun newInstance(initialCard: CardItem?): EditCardDialogFragment {
            val fragment = EditCardDialogFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARG_CARD_ITEM, initialCard)
            fragment.arguments = bundle
            return fragment
        }
    }
}
