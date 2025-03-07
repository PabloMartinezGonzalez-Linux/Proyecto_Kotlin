package com.example.login.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CardRequest(
    val id: Int? = null,
    @SerialName("photo")
    val imageBase64: String,
    @SerialName("name")
    val brand: String,
    @SerialName("description")
    val model: String,
    val averageRating: Double,
    val hasImprovements: Boolean
)
