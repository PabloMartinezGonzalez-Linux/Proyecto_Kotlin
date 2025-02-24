package com.example.login.data.repository

import com.example.login.domain.models.CardItem
import com.example.login.domain.repository.CardRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepositoryImpl @Inject constructor() : CardRepository {
    private val cards = mutableListOf<CardItem>()

    override fun addCard(card: CardItem) {
        cards.add(card)
    }

    override fun deleteCard(card: CardItem) {
        cards.remove(card)
    }

    override fun updateCard(card: CardItem) {
        val index = cards.indexOfFirst { it == card }
        if (index != -1) {
            cards[index] = card
        }
    }

    override fun getAllCards(): List<CardItem> = cards
}

