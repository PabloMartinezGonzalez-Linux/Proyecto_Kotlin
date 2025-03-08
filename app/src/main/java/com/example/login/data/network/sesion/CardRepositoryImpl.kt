package com.example.login.data.network.sesion

import android.util.Log
import com.example.login.data.network.services.CardService
import com.example.login.domain.models.CardItem
import com.example.login.domain.models.CardRequest
import com.example.login.domain.repository.CardRepository
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(
    private val cardService: CardService
) : CardRepository {

    override suspend fun getCards(): Result<List<CardItem>> {
        return try {
            val response = cardService.getCards()

            Log.d("CardRepositoryImpl", "Código de respuesta: ${response.code()}")
            Log.d("CardRepositoryImpl", "Cuerpo de respuesta: ${response.body()}")

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía"))
            } else {
                Result.failure(Exception("Error al obtener las cards: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Log.e("CardRepositoryImpl", "Error en la solicitud de cards", e)
            Result.failure(e)
        }
    }

    override suspend fun addCard(newCard: CardRequest): Result<CardItem> {
        return try {
            val response = cardService.addCard(newCard)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)  // 🔹 Devuelve la card creada
                } ?: Result.failure(Exception("Error: Respuesta vacía"))
            } else {
                Result.failure(Exception("Error: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
