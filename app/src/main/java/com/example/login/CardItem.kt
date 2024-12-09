package com.example.login

import android.net.Uri

data class CardItem(
    var imageRes: Int?, // Recurso de imagen predeterminado
    var brand: String,
    var model: String,
    var imageUri: Uri? = null, // Imagen seleccionada por el usuario
    var bgImageRes: Int = R.drawable.bg_gris // Imagen de fondo, valor por defecto
)

