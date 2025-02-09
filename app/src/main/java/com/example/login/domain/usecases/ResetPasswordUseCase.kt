package com.example.login.domain.usecases

import com.example.login.domain.repository.AuthRepository

class ResetPasswordUseCase(private val authRepository: AuthRepository) {
    suspend fun execute(email: String) =
        authRepository.resetPassword(email)
}
