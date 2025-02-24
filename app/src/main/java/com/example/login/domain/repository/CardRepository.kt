package com.example.login.domain.repository

import com.example.login.domain.models.CardItem

interface CardRepository {
    fun addCard(card: CardItem)
    fun deleteCard(card: CardItem)
    fun updateCard(card: CardItem)
    fun getAllCards(): List<CardItem>
}
