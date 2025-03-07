package com.example.login.data.network.services

import com.example.login.data.models.CardRequest
import com.example.login.data.models.CardResponse
import retrofit2.Response
import retrofit2.http.*

interface CardService {
    @POST("cards")
    suspend fun createCard(@Body request: CardRequest): Response<Unit> // ✅ Devuelve Response<Unit> en lugar de String

    @GET("cards")
    suspend fun getCards(): Response<List<CardResponse>> // ✅ Devuelve Response<List<CardResponse>>

    @GET("cards/{id}")
    suspend fun getCardById(@Path("id") id: Int): Response<CardResponse> // ✅ Devuelve Response<CardResponse>

    @PUT("cards/{id}")
    suspend fun updateCard(@Path("id") id: Int, @Body request: CardRequest): Response<Unit> // ✅ Usa Response<Unit>

    @DELETE("cards/{id}")
    suspend fun deleteCard(@Path("id") id: Int): Response<Unit> // ✅ Usa Response<Unit>
}
