package com.example.login.domain.usecases

import com.example.login.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun execute(username: String, email: String, password: String): Result<Unit> {
        return authRepository.register(username, email, password)
    }
}
