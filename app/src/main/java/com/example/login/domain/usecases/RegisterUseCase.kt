package com.example.login.domain.usecases

import com.example.login.domain.repository.AuthRepository

class RegisterUseCase(private val authRepository: AuthRepository) {
    suspend fun execute(email: String, password: String) =
        authRepository.register(email, password)
}
