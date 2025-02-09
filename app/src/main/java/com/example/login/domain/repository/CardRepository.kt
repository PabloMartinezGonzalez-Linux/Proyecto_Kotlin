package com.example.login.domain.repository

import com.example.login.domain.models.CardItem

interface CardRepository {
    fun getAllCards(): List<CardItem>
    fun addCard(card: CardItem)
    fun deleteCard(card: CardItem)
}
