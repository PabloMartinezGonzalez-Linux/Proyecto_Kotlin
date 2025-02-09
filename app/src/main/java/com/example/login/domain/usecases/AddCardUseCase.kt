package com.example.login.domain.usecases

import com.example.login.domain.models.CardItem
import com.example.login.domain.repository.CardRepository

class AddCardUseCase(private val repository: CardRepository) {
    fun execute(card: CardItem) {
        repository.addCard(card)
    }
}
