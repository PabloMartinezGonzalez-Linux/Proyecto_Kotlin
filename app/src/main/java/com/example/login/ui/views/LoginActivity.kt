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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observa el estado de autenticación
        viewModel.authState.observe(this) { state ->
            when (state) {
                is AuthState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.loginButton.isEnabled = false
                    binding.registerButton.isEnabled = false
                }
                is AuthState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.loginButton.isEnabled = true
                    binding.registerButton.isEnabled = true
                    // Navegar a la pantalla de tarjetas
                    val intent = Intent(this, Cardview::class.java)
                    startActivity(intent)
                    binding.root.postDelayed({ finish() }, 300) // Evita cerrar antes de que se cargue la nueva pantalla
                }
                is AuthState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.loginButton.isEnabled = true
                    binding.registerButton.isEnabled = true
                    showAlert(state.message)
                }
                else -> Unit
            }
        }

        binding.loginButton.setOnClickListener {
            binding.apply {
                val username = usernameEditText.text.toString().trim()
                val pass = passwordEditText.text.toString().trim()

                if (username.isBlank() || pass.isBlank()) {
                    showAlert("Por favor, completa todos los campos.")
                    return@setOnClickListener
                }

                loginButton.isEnabled = false // Deshabilitar botón para evitar spam de clics
                viewModel.login(username, pass)
            }
        }

        binding.registerButton.setOnClickListener {
            binding.apply {
                val username = usernameEditText.text.toString().trim()
                val pass = passwordEditText.text.toString().trim()
                val email = "$username@example.com" // 🔹 Se genera automáticamente a partir del username

                if (username.isBlank() || pass.isBlank()) {
                    showAlert("Por favor, completa todos los campos.")
                    return@setOnClickListener
                }

                registerButton.isEnabled = false // Evitar clics repetidos
                viewModel.register(username, email, pass)
            }
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
