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

class Login : AppCompatActivity() {

    private lateinit var binding: LoginBinding
    private val viewModel: LoginViewModel by viewModels()

    // Propiedades convenientes para obtener email y password desde el binding
    private val email get() = binding.email.text.toString().trim()
    private val pass get() = binding.pass.text.toString().trim()

    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = resources.getColor(R.color.negro100)
        super.onCreate(savedInstanceState)

        // Verificamos si ya existe un usuario autenticado y verificado en Firebase
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null && firebaseUser.isEmailVerified) {
            // Si ya existe, navegamos a la siguiente Activity sin mostrar el login
            val intent = Intent(this, Cardview::class.java)
            startActivity(intent)
            finish()
            return
        }

        // Si no hay usuario autenticado, se configura el binding y se muestra la pantalla de login
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observamos el estado de autenticación del ViewModel
        viewModel.authState.observe(this) { state ->
            when (state) {
                is AuthState.Loading -> {
                    // Aquí puedes mostrar un indicador de carga, si lo deseas
                }
                is AuthState.Success -> {
                    // Navegar a la siguiente pantalla (por ejemplo, Cardview)
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
