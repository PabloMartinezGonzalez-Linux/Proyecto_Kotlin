package com.example.login.domain.usecases

import com.example.login.domain.repository.CardRepository
import javax.inject.Inject

class DeleteCardUseCase @Inject constructor(
    private val cardRepository: CardRepository
) {
    suspend fun execute(cardId: Int): Result<Unit> {
        return cardRepository.deleteCard(cardId)
    }
}
