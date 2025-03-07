package com.example.login.domain.repository

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<String>
    suspend fun register(username: String, email: String, password: String): Result<Unit>
    suspend fun resetPassword(email: String): Result<Unit>
}
