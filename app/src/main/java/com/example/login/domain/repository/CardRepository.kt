package com.example.login.domain.repository

import com.example.login.domain.models.CardItem
import com.example.login.domain.models.CardRequest

interface CardRepository {
    suspend fun getCards(): Result<List<CardItem>>
    suspend fun addCard(newCard: CardRequest): Result<CardItem>
    suspend fun deleteCard(cardId: Int): Result<Unit>
}
