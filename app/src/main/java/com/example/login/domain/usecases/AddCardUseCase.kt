package com.example.login.domain.usecases

import com.example.login.domain.models.CardItem
import com.example.login.domain.models.CardRequest
import com.example.login.domain.repository.CardRepository
import javax.inject.Inject

class AddCardUseCase @Inject constructor(
    private val cardRepository: CardRepository
) {
    suspend fun execute(newCard: CardRequest): Result<CardItem> {
        return cardRepository.addCard(newCard)
    }
}
