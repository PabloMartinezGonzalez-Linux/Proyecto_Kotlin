package com.example.login.data.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String
)
