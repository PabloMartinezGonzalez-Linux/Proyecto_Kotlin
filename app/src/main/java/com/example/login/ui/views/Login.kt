package com.example.login.ui.views

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.login.R
import com.example.login.databinding.LoginBinding
import com.example.login.ui.viewmodel.AuthState
import com.example.login.ui.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Login : AppCompatActivity() {

    private lateinit var binding: LoginBinding
    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val email get() = binding.email.text.toString().trim()
    private val pass get() = binding.pass.text.toString().trim()

    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = resources.getColor(R.color.negro100)
        super.onCreate(savedInstanceState)

        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null && firebaseUser.isEmailVerified) {
            val intent = Intent(this, Cardview::class.java)
            startActivity(intent)
            finish()
            return
        }

        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.authState.observe(this) { state ->
            when (state) {
                is AuthState.Loading -> {
                }
                is AuthState.Success -> {
                    val intent = Intent(this, Cardview::class.java)
                    startActivity(intent)
                    finish()
                }
                is AuthState.Error -> {
                    showAlert(state.message)
                }
                else -> Unit
            }
        }

        binding.registerButton.setOnClickListener {
            if (email.isBlank() || pass.isBlank()) {
                showAlert("Por favor, completa todos los campos.")
                return@setOnClickListener
            }
            viewModel.register(email, pass)
        }

        binding.loginButton.setOnClickListener {
            if (email.isBlank() || pass.isBlank()) {
                showAlert("Por favor, completa todos los campos.")
                return@setOnClickListener
            }
            viewModel.login(email, pass)
        }

        binding.resetPass.setOnClickListener {
            if (email.isBlank()) {
                showAlert("Por favor, introduce un correo electrónico válido.")
                return@setOnClickListener
            }
            viewModel.resetPassword(email)
        }
    }

    private fun showAlert(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("Aceptar", null)
            .create()
            .show()
    }
}
