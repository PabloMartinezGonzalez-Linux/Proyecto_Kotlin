package com.example.login.domain.usecases

import com.example.login.domain.models.CardItem
import com.example.login.domain.repository.CardRepository
import javax.inject.Inject

class GetCardsUseCase @Inject constructor(
    private val repository: CardRepository
) {
    fun execute(): List<CardItem> {
        return repository.getAllCards()
    }
}
