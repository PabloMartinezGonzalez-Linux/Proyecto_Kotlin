package com.example.login.data.repository

import android.util.Log
import com.example.login.data.models.CardRequest
import com.example.login.data.models.CardResponse
import com.example.login.data.network.services.CardService
import com.example.login.domain.repository.CardRepository
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(
    private val cardService: CardService
) : CardRepository {

    override suspend fun createCard(card: CardRequest): Result<Unit> {
        return try {
            val response = cardService.createCard(card)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al crear la tarjeta: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCards(): Result<List<CardResponse>> {
        return try {
            val response = cardService.getCards()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía"))
            } else {
                val errorMsg = "Código: ${response.code()}, Mensaje: ${response.message()}, ErrorBody: ${response.errorBody()?.string()}"
                Log.e("CardRepositoryImpl", errorMsg)
                Result.failure(Exception("Error al obtener tarjetas: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun getCardById(id: Int): Result<CardResponse> {
        return try {
            val response = cardService.getCardById(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Tarjeta no encontrada"))
            } else {
                Result.failure(Exception("Error al obtener la tarjeta: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateCard(id: Int, card: CardRequest): Result<Unit> {
        return try {
            val response = cardService.updateCard(id, card)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al actualizar la tarjeta: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteCard(id: Int): Result<Unit> {
        return try {
            val response = cardService.deleteCard(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar la tarjeta: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
