package com.example.login.domain.usecases

import com.example.login.domain.models.CardItem
import com.example.login.domain.repository.CardRepository

class DeleteCardUseCase(private val repository: CardRepository) {
    fun execute(card: CardItem) {
        repository.deleteCard(card)
    }
}
