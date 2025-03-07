package com.example.login.data.repository

import com.example.login.data.models.LoginRequest
import com.example.login.data.models.RegisterRequest
import com.example.login.data.network.TokenManager
import com.example.login.data.network.services.AuthService
import com.example.login.domain.repository.AuthRepository
import retrofit2.Retrofit
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val retrofit: Retrofit
) : AuthRepository {

    private val authService = retrofit.create(AuthService::class.java)

    override suspend fun login(username: String, password: String): Result<String> {
        return try {
            val response = authService.login(LoginRequest(username, password))

            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    TokenManager.saveToken(loginResponse.token)
                    Result.success(loginResponse.token)
                } ?: Result.failure(Exception("Respuesta vacía"))
            } else {
                Result.failure(Exception("Error: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(username: String, email: String, password: String): Result<Unit> {
        return try {
            val response = authService.register(RegisterRequest(username, email, password))

            if (response.isSuccessful) {
                println("Registro exitoso: ${response.body()?.message}") // ✅ Verifica en Logcat
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error en el registro: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            // Implementar la lógica si tu API soporta reset de contraseña.
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
