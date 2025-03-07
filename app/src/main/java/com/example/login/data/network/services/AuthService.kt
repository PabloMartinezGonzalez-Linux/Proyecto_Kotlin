package com.example.login.data.network.services

import com.example.login.data.models.LoginRequest
import com.example.login.data.models.LoginResponse
import com.example.login.data.models.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse> // ✅ Usa Response<LoginResponse>

    data class RegisterResponse(val message: String)

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

}
