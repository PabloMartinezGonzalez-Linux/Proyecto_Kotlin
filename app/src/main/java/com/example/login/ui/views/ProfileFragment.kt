package com.example.login.ui.views

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.login.databinding.FragmentPerfilBinding
import com.example.login.domain.models.UserProfile
import com.example.login.ui.viewmodel.UserProfileViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    private lateinit var getImageLauncher: ActivityResultLauncher<String>
    private var selectedImageUri: Uri? = null

    private val userProfileViewModel: UserProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                binding.ivProfilePhoto.setImageURI(it)
            }
        }

        binding.ivProfilePhoto.setOnClickListener {
            getImageLauncher.launch("image/*")
        }

        binding.btnSave.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(requireContext(), "El email no puede estar vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val profile = UserProfile(email = email, photoUri = selectedImageUri)
            userProfileViewModel.updateUserProfile(profile)

            Toast.makeText(requireContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
