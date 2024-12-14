package com.example.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.login.databinding.LoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var binding: LoginBinding

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

        val email = binding.email.text
        val pass = binding.pass.text

        binding.registerButton.setOnClickListener {
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email.toString(), pass.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = FirebaseAuth.getInstance().currentUser
                            if (user != null) {
                                user.sendEmailVerification().addOnCompleteListener { verificationTask ->
                                    if (verificationTask.isSuccessful) {
                                        showSuccess("Registro exitoso. Por favor, verifica tu correo antes de iniciar sesión.")
                                    } else {
                                        showAlert("Error al enviar el correo de verificación.")
                                    }
                                }
                            } else {
                                showAlert("Error inesperado. No se pudo obtener el usuario.")
                            }
                        } else {
                            showAlert("Error en el registro: ${task.exception?.message}")
                        }
                    }
            } else {
                showAlert("Por favor, completa todos los campos.")
            }
        }

        binding.loginButton.setOnClickListener {
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email.toString(), pass.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = FirebaseAuth.getInstance().currentUser
                            if (user != null) {
                                if (user.isEmailVerified) {
                                    // Guardar estado de logueo en SharedPreferences
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
            } else {
                showAlert("Por favor, completa todos los campos.")
            }
        }

        binding.resetPass.setOnClickListener {
            val email = binding.email.text.toString()

            if (email.isNotEmpty()) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showSuccessDialog()
                        } else {
                            showErrorDialog(task.exception?.message)
                        }
                    }
            } else {
                showErrorDialog("Por favor, introduce un correo electrónico válido.")
            }
        }

    }

    private fun showSuccessDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Correo enviado")
        builder.setMessage("Se ha enviado un correo de recuperación de contraseña. Revisa tu bandeja de entrada.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showErrorDialog(message: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(message ?: "Se produjo un error. Inténtalo de nuevo.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showSuccess(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Éxito")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
