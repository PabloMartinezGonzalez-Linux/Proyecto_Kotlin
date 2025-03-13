package com.example.login.data.network.services

import com.example.login.domain.models.CardItem
import com.example.login.domain.models.CardRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CardService {
    @GET("cards")
    suspend fun getCards(): Response<List<CardItem>>

    @POST("/cards")
    suspend fun addCard(@Body newCard: CardRequest): Response<CardItem>

    @DELETE("cards/{id}")
    suspend fun deleteCard(@Path("id") cardId: Int): Response<Unit>

    @PUT("/cards/{id}")
    suspend fun updateCard(@Path("id") cardId: Int, @Body updatedCard: CardRequest): Response<CardItem>
}




