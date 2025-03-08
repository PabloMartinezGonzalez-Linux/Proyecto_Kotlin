package com.example.login.domain.usecases

import com.example.login.domain.models.CardItem
import com.example.login.domain.models.CardRequest
import com.example.login.domain.repository.CardRepository
import javax.inject.Inject

class UpdateCardUseCase @Inject constructor(
    private val cardRepository: CardRepository
) {
    suspend fun execute(cardId: Int, updatedCard: CardRequest): Result<CardItem> {
        return cardRepository.updateCard(cardId, updatedCard)
    }
}
