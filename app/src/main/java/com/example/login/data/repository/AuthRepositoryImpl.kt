package com.example.login.data.repository

import android.util.Log
import com.example.login.data.models.LoginRequest
import com.example.login.data.models.RegisterRequest
import com.example.login.data.network.services.AuthService
import com.example.login.data.network.sesion.SessionManager
import com.example.login.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val sessionManager: SessionManager // ✅ Inyectamos SessionManager
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<String> {
        return try {
            val request = LoginRequest(username, password)
            Log.d("AuthRepositoryImpl", "Enviando solicitud de login: $request")

            val response = authService.login(request)

            Log.d("AuthRepositoryImpl", "Código de respuesta: ${response.code()}")
            Log.d("AuthRepositoryImpl", "Cuerpo de respuesta: ${response.body()}")
            Log.d("AuthRepositoryImpl", "Error Body: ${response.errorBody()?.string()}")

            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    sessionManager.saveAuthToken(loginResponse.token) // ✅ Guardamos el token
                    Result.success(loginResponse.token)
                } ?: Result.failure(Exception("Respuesta vacía"))
            } else {
                Result.failure(Exception("Error: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "Error en la solicitud de login", e)
            Result.failure(e)
        }
    }

    override suspend fun register(username: String, email: String, password: String): Result<Unit> {
        return try {
            val request = RegisterRequest(username, email, password)
            Log.d("AuthRepositoryImpl", "Enviando solicitud de registro: $request")

            val response = authService.register(request)

            Log.d("AuthRepositoryImpl", "Código de respuesta: ${response.code()}")
            Log.d("AuthRepositoryImpl", "Cuerpo de respuesta: ${response.body()}")
            Log.d("AuthRepositoryImpl", "Error Body: ${response.errorBody()?.string()}")

            if (response.isSuccessful) {
                Log.d("AuthRepositoryImpl", "Registro exitoso: ${response.body()?.message}")
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error en el registro: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "Error en la solicitud de registro", e)
            Result.failure(e)
        }
    }
}
