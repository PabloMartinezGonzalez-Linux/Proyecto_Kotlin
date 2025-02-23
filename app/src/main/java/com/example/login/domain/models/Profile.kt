package com.example.login.domain.models

import android.net.Uri

data class UserProfile(
    val email: String,
    val photoUri: Uri? = null
)
