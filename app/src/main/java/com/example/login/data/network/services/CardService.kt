package com.example.login.data.network.services

import com.example.login.domain.models.CardItem
import com.example.login.domain.models.CardRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CardService {
    @GET("cards")
    suspend fun getCards(): Response<List<CardItem>>

    @POST("/cards")
    suspend fun addCard(@Body newCard: CardRequest): Response<CardItem>
}




