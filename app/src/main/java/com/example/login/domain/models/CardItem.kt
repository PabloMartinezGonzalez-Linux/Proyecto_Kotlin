package com.example.login.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class CardItem(
    val id: Int? = null,
    val userId: Int? = null,
    val photo: String,
    val name: String,
    val description: String,
    val averageRating: Double,
    val hasImprovements: Boolean
)
