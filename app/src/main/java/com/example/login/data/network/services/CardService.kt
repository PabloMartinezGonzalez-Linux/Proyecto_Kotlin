package com.example.login.data.network.services

import com.example.login.domain.models.CardItem
import retrofit2.Response
import retrofit2.http.GET

interface CardService {
    @GET("cards")
    suspend fun getCards(): Response<List<CardItem>>
}
