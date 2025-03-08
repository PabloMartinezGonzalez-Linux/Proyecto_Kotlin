package com.example.login.domain.repository

import com.example.login.domain.models.CardItem

interface CardRepository {
    suspend fun getCards(): Result<List<CardItem>>
}
