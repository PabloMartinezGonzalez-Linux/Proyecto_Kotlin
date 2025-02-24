package com.example.login.domain.usecases

import com.example.login.domain.models.CardItem
import com.example.login.domain.repository.CardRepository
import javax.inject.Inject

class UpdateCardUseCase @Inject constructor(
    private val repository: CardRepository
) {
    fun execute(card: CardItem) {
        repository.updateCard(card)
    }
}
