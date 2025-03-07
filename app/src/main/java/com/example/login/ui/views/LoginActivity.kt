package com.example.login.ui.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.login.databinding.LoginBinding
import com.example.login.ui.viewmodel.AuthState
import com.example.login.ui.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginBinding
    private val viewModel: LoginViewModel by viewModels()

    // Suponiendo que en tu layout tienes campos de entrada llamados usernameEditText y passwordEditText
    private val username get() = binding.usernameEditText.text.toString().trim()
    private val pass get() = binding.passwordEditText.text.toString().trim()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observa el estado de autenticación
        viewModel.authState.observe(this) { state ->
            when (state) {
                is AuthState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is AuthState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    // Login exitoso, navega a la siguiente pantalla (por ejemplo, Cardview)
                    val intent = Intent(this, Cardview::class.java)
                    startActivity(intent)
                    finish()
                }
                is AuthState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showAlert(state.message)
                }
                else -> Unit
            }
        }

        binding.loginButton.setOnClickListener {
            if (username.isBlank() || pass.isBlank()) {
                showAlert("Por favor, completa todos los campos.")
                return@setOnClickListener
            }
            viewModel.login(username, pass)
        }

        binding.registerButton.setOnClickListener {
            if (username.isBlank() || pass.isBlank()) {
                showAlert("Por favor, completa todos los campos.")
                return@setOnClickListener
            }
            // Para el registro, se asume que se usará el username también como correo, o podrías tener otro campo
            viewModel.register(username, "$username@example.com", pass)
        }

        binding.resetPass.setOnClickListener {
            if (username.isBlank()) {
                showAlert("Por favor, introduce un correo electrónico válido.")
                return@setOnClickListener
            }
            viewModel.resetPassword(username)
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
