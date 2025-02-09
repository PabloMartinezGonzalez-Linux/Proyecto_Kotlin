package com.example.login.ui.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.login.R
import com.example.login.databinding.LoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var binding: LoginBinding

    private val email get() = binding.email.text.toString().trim()
    private val pass get() = binding.pass.text.toString().trim()

    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = resources.getColor(R.color.negro100)
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            val intent = Intent(this, Cardview::class.java)
            startActivity(intent)
            finish()
        }

        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            Log.d("LoginDebug", "Register - Email: '$email', Password: '$pass'")

            if (email.isBlank() || pass.isBlank()) {
                showAlert("Por favor, completa todos los campos.")
                return@setOnClickListener
            }

            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        user?.sendEmailVerification()?.addOnCompleteListener { verificationTask ->
                            if (verificationTask.isSuccessful) {
                                showSuccess("Registro exitoso. Por favor, verifica tu correo antes de iniciar sesión.")
                            } else {
                                showAlert("Error al enviar el correo de verificación.")
                            }
                        }
                    } else {
                        showAlert("Error en el registro: ${task.exception?.message}")
                    }
                }
        }

        binding.loginButton.setOnClickListener {
            Log.d("LoginDebug", "Login - Email: '$email', Password: '$pass'")

            if (email.isBlank() || pass.isBlank()) {
                showAlert("Por favor, completa todos los campos.")
                return@setOnClickListener
            }

            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        if (user != null) {
                            if (user.isEmailVerified) {
                                val editor = sharedPreferences.edit()
                                editor.putBoolean("isLoggedIn", true)
                                editor.apply()

                                val intent = Intent(this, Cardview::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                showAlert("Por favor, verifica tu correo antes de iniciar sesión.")
                            }
                        } else {
                            showAlert("Error inesperado. No se pudo obtener el usuario.")
                        }
                    } else {
                        showAlert("Error en el inicio de sesión: ${task.exception?.message}")
                    }
                }
        }

        binding.resetPass.setOnClickListener {
            if (email.isBlank()) {
                showErrorDialog("Por favor, introduce un correo electrónico válido.")
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showSuccessDialog()
                    } else {
                        showErrorDialog(task.exception?.message)
                    }
                }
        }
    }

    private fun showSuccessDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Correo enviado")
        builder.setMessage("Se ha enviado un correo de recuperación de contraseña. Revisa tu bandeja de entrada.")
        builder.setPositiveButton("Aceptar", null)
        builder.create().show()
    }

    private fun showErrorDialog(message: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(message ?: "Se produjo un error. Inténtalo de nuevo.")
        builder.setPositiveButton("Aceptar", null)
        builder.create().show()
    }

    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        builder.create().show()
    }

    private fun showSuccess(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Éxito")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        builder.create().show()
    }
}
