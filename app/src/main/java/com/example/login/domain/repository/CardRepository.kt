package com.example.login.domain.repository

import com.example.login.data.models.CardRequest
import com.example.login.data.models.CardResponse

interface CardRepository {
    suspend fun createCard(card: CardRequest): Result<Unit>
    suspend fun getCards(): Result<List<CardResponse>>
    suspend fun getCardById(id: Int): Result<CardResponse>
    suspend fun updateCard(id: Int, card: CardRequest): Result<Unit>
    suspend fun deleteCard(id: Int): Result<Unit>
}
