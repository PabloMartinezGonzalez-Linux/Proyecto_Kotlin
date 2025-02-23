package com.example.login.domain.usecases

import com.example.login.domain.repository.AuthRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun execute(email: String) =
        authRepository.resetPassword(email)
}
