package com.example.login.data.repository

import com.example.login.data.datasource.CardLocalDataSource
import com.example.login.domain.models.CardItem
import com.example.login.domain.repository.CardRepository

class CardRepositoryImpl(private val localDataSource: CardLocalDataSource) : CardRepository {
    override fun getAllCards(): List<CardItem> {
        return localDataSource.getCards()
    }

    override fun addCard(card: CardItem) {
        val cards = localDataSource.getCards().toMutableList()
        cards.add(card)
        localDataSource.saveCards(cards)
    }

    override fun deleteCard(card: CardItem) {
        val cards = localDataSource.getCards().toMutableList()
        cards.remove(card)
        localDataSource.saveCards(cards)
    }
}
