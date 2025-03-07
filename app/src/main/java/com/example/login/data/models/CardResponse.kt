package com.example.login.data.models

import com.example.login.domain.models.CardItem

@kotlinx.serialization.Serializable
data class CardResponse(
    val id: Int,
    val userId: Int,
    val photo: String,           // 🔥 Este es el Base64 de la imagen
    val name: String,            // 🔥 Esto es el equivalente a "brand"
    val description: String,      // 🔥 Esto sería el "model"
    val averageRating: Double,
    val hasImprovements: Boolean
) {
    fun toCardItem(): CardItem {
        return CardItem(
            id = id,
            brand = name,             // 🔥 Mapea `name` a `brand`
            model = description,      // 🔥 Mapea `description` a `model`
            imageBase64 = photo       // 🔥 Mapea `photo` a `imageBase64`
        )
    }
}
