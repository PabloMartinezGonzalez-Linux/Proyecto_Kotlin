package com.example.login.domain.models

import android.net.Uri

data class CardItem(
    var imageRes: Int?,
    var brand: String,
    var model: String,
    var imageUri: Uri? = null,
    var bgImageRes: Int = 0
)
